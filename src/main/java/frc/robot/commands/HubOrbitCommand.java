package frc.robot.commands;
 
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.drive.DriveSubsystem;
 
/**
 * HubOrbitCommand
 *
 * While the trigger is held:
 *   1. Generates and follows a PathPlanner path to the closest
 *      point on the 3-meter semicircle around the hub that is on the
 *      robot's alliance side.
 *   2. Once on the arc: left-right joystick input slides the robot
 *      along the arc; the heading PID always points the robot at the hub.
 */
public class HubOrbitCommand extends Command {

    /** Center of the hub in field-relative metres */
    private final Translation2d HUB_CENTER;
 
    /** Radius of the orbit arc in metres */
    private static final double ORBIT_RADIUS = 3.0;
 
    /**
     * Robot is considered "on the arc" when it is within this distance (m).
     * Tune upward if the robot oscillates between pathing and orbit mode.
     */
    private static final double ON_ARC_TOLERANCE = 0.15;
 
    /** Maximum tangential speed (m/s) the joystick can command along the arc */
    private static final double MAX_ARC_SPEED = 3.0;
 
    /** Deadband for the orbit joystick axis */
    private static final double JOYSTICK_DEADBAND = 0.08;
 
    // PathPlanner constraints for the "snap to arc" path
    private static final PathConstraints PATH_CONSTRAINTS =
            new PathConstraints(3.5, 3.0, Math.toRadians(540), Math.toRadians(720));
 
    // Heading PID  (input: heading error in radians, output: rotational speed rad/s)
    // Start with kP ≈ 5–8; add kD ≈ 0.1–0.3 to damp oscillation.
    private static final double HEADING_KP = 6.0;
    private static final double HEADING_KI = 0.0;
    private static final double HEADING_KD = 0.15;
    private static final double MAX_HEADING_RATE = Math.toRadians(360); // rad/s cap
 
 
    private final DriveSubsystem drive;
    private final DoubleSupplier orbitAxisSupplier; // left-right joystick, +1 = right
    private final boolean isRedAlliance;
 
    private final PIDController headingPID;
 
    private Command pathCommand = null; // active path-following command
    private boolean onArc = false;
 
 
    /**
     * @param drive            Swerve drive subsystem
     * @param orbitAxis        Joystick supplier; positive = move clockwise on the arc
     * @param isRedAlliance    Pass DriverStation.getAlliance() == Alliance.Red
     */
    public HubOrbitCommand(DriveSubsystem drive, DoubleSupplier orbitAxis, boolean isRedAlliance) {

        this.drive = drive;
        this.orbitAxisSupplier = orbitAxis;
        this.isRedAlliance = isRedAlliance;
        HUB_CENTER = isRedAlliance ? Constants.RED_HUB : Constants.BLUE_HUB;
 
        headingPID = new PIDController(HEADING_KP, HEADING_KI, HEADING_KD);
        headingPID.enableContinuousInput(-Math.PI, Math.PI);
 
        addRequirements(drive);
    }
 
    @Override
    public void initialize() {
        onArc = false;
        headingPID.reset();
        schedulePathToArc();
        System.out.println("path scheduled");
    }
 
    @Override
    public void execute() {
        Pose2d pose = drive.getPose();
        double distToHub = pose.getTranslation().getDistance(HUB_CENTER);
        double distToArc = Math.abs(distToHub - ORBIT_RADIUS);
 
        if (distToArc <= ON_ARC_TOLERANCE) {
            onArc = true;
            System.out.println("on arc");
        }
 
        if (onArc && (pathCommand == null || !pathCommand.isScheduled())) {
           runOrbitMode(pose);
           System.out.println("can begin orbiting");
        }
    }
 
    @Override
    public void end(boolean interrupted) {
        if (pathCommand != null && pathCommand.isScheduled()) {
            pathCommand.cancel();
        }
        DriveCommands.stopDriveCommand(drive);
    }
 
    @Override
    public boolean isFinished() {
        return false; // runs until button is released
    }

 
    /**
     * Builds a path from the robot's current pose
     * to the closest legal point on the semicircle, then schedules it.
     */
    private void schedulePathToArc() {
        Pose2d currentPose = drive.getPose();
        Translation2d target = closestArcPoint(currentPose.getTranslation());
 
        // Heading at the target: face the hub
        Rotation2d targetHeading = headingToHub(target);

        // Path to hub
        pathCommand = AutoBuilder.pathfindToPose(new Pose2d(target, targetHeading), PATH_CONSTRAINTS, 0.0);
        pathCommand.schedule();
    }
 
 
    /**
     * Maps the joystick to tangential speed along the arc and blends with
     * a heading correction so the robot always faces the hub.
     */
    private void runOrbitMode(Pose2d pose) {
        // --- 1. Current angle around the hub ---
        Translation2d toRobot = pose.getTranslation().minus(HUB_CENTER);
        double currentAngle = Math.atan2(toRobot.getY(), toRobot.getX()); // radians
 
        // --- 2. Joystick → tangential velocity ---
        double rawAxis = MathUtil.applyDeadband(orbitAxisSupplier.getAsDouble(), JOYSTICK_DEADBAND);
        // apply basic deadband to joystick 
        double tangentialSpeed = rawAxis * MAX_ARC_SPEED; // m/s along arc
        // relate joystick with movement along arc
 
        // --- 3. Ideal position: keep robot ON the arc ---
        Translation2d idealPos = new Translation2d(
                HUB_CENTER.getX() + ORBIT_RADIUS * Math.cos(currentAngle),
                HUB_CENTER.getY() + ORBIT_RADIUS * Math.sin(currentAngle)
        );

        // Radial correction: nudge robot toward ideal position
        Translation2d radialError = idealPos.minus(pose.getTranslation());
        // vector from current to ideal pose on arc

        // Gentle proportional correction — tune kR as needed
        double kR = 2.0;
        double vxCorrect = radialError.getX() * kR; // when error is bigger correction velocity is faster
        double vyCorrect = radialError.getY() * kR;
 
        // --- 4. Tangent direction at current angle ---
        //    to move along the arc, robot needs to move along vector tangent to current vector from hub
        //    The unit tangent (counter-clockwise positive) is (-sin θ, +cos θ).
        //    Negate it to make positive joystick = clockwise (towards alliance wall).
        double tangentX = -Math.sin(currentAngle);
        double tangentY =  Math.cos(currentAngle);
 
        double vxField = tangentX * tangentialSpeed + vxCorrect;
        double vyField = tangentY * tangentialSpeed + vyCorrect;
        // vField = (direction along arc x speed) + correction towards arc
        // this also splits speed in the proper ratio for x and y directions
 
        // --- 5. Heading PID — always face hub ---
        double desiredHeadingRad = headingToHub(pose.getTranslation()).getRadians();
        double currentHeadingRad = pose.getRotation().getRadians();
        double omega = headingPID.calculate(currentHeadingRad, desiredHeadingRad); 
        // returns rotation correction speed radians/sec 
        omega = MathUtil.clamp(omega, -MAX_HEADING_RATE, MAX_HEADING_RATE);
        // constraits to prevent crazy rotation correction
 
        // --- 6. Send to drive ---
        //    fromFieldRelativeSpeeds converts field-frame vx/vy to robot-frame.
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                vxField, vyField, omega, pose.getRotation()
        );
        drive.runVelocity(speeds);
    }
 
 
    /**
     * Returns the point on the orbit semicircle closest to current {@code robotPos},
     */
    private Translation2d closestArcPoint(Translation2d robotPos) {
        Translation2d toRobot = robotPos.minus(HUB_CENTER); // finds vector of hub to robot
        double angle = Math.atan2(toRobot.getY(), toRobot.getX()); // extracts angle from hub to robot

        if (isRedAlliance) {
            angle = MathUtil.clamp(angle, -Math.PI / 2.0, Math.PI / 2.0);
        } else {
            // Clamp to [π/2, π] ∪ [-π, -π/2] — keep angle on left half
            if (angle > 0) angle = MathUtil.clamp(angle, Math.PI / 2.0, Math.PI);
            else           angle = MathUtil.clamp(angle, -Math.PI, -Math.PI / 2.0);
        }
        // Red Alliance: robot should be on the right half of hub → angles in [-π/2, +π/2].
        // Blue alliance: robot shuld be on left half → angles in [π/2, 3π/2] (or [-π, -π/2] ∪ [π/2, π])
        // if angle out of bounds then it is clamped to closest in bound angle
        // Bounds might need to be adjusted
        return new Translation2d(
                HUB_CENTER.getX() + ORBIT_RADIUS * Math.cos(angle),
                HUB_CENTER.getY() + ORBIT_RADIUS * Math.sin(angle)
        ); 
        // a point on unit circle is (cos(angle),sin(angle),)
        // multiply the coordinates by the orbit radius to scale up to orbit arc
        // then add hub coordinates to scale to correct position on field
        // this gives us ideal robot pose on arc
    }
 
    /**
     * Returns the rotation that makes the robot face the hub from {@code pos}.
     */
    private Rotation2d headingToHub(Translation2d pos) {
        Translation2d toHub = HUB_CENTER.minus(pos);
        // vector of robot to hub
        return new Rotation2d(Math.atan2(toHub.getY(), toHub.getX()));
        // extract angle from where robot is facing to hub
    }
}