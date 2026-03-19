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
 *   1. On entry: generates and follows a PathPlanner path to the closest
 *      point on the 3-metre semicircle around the hub that is on the
 *      robot's alliance side.
 *   2. Once on the arc: left-right joystick input slides the robot
 *      along the arc; the heading PID always points the robot at the hub.
 *
 * Coordinate convention: hub centre is at HUB_CENTRE (field-relative).
 * The "alliance semicircle" is the half of the circle with X >= hub X
 * for the red alliance, or X <= hub X for the blue alliance — adjust
 * HUB_CENTRE and the clamp logic to match your field layout.
 */
public class HubOrbitCommand extends Command {

 
    /** Centre of the hub in field-relative metres */
    private final Translation2d HUB_CENTRE;
 
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
 
    private Command pathCommand = null; // active PPLIB path-following command
    private boolean onArc = false;
 
 
    /**
     * @param drive            Your swerve drive subsystem
     * @param orbitAxis        Joystick supplier; positive = move clockwise on the arc
     * @param isRedAlliance    Pass DriverStation.getAlliance() == Alliance.Red
     */
    public HubOrbitCommand(DriveSubsystem drive,
                           DoubleSupplier orbitAxis,
                           boolean isRedAlliance) {
        this.drive = drive;
        this.orbitAxisSupplier = orbitAxis;
        this.isRedAlliance = isRedAlliance;
        HUB_CENTRE = isRedAlliance ? Constants.RED_HUB : Constants.BLUE_HUB;
 
        headingPID = new PIDController(HEADING_KP, HEADING_KI, HEADING_KD);
        headingPID.enableContinuousInput(-Math.PI, Math.PI);
 
        addRequirements(drive);
    }
 
    @Override
    public void initialize() {
        onArc = false;
        headingPID.reset();
        schedulePathToArc();
    }
 
    @Override
    public void execute() {
        Pose2d pose = drive.getPose();
        double distToHub = pose.getTranslation().getDistance(HUB_CENTRE);
        double distToArc = Math.abs(distToHub - ORBIT_RADIUS);
 
        if (distToArc <= ON_ARC_TOLERANCE) {
            onArc = true;
        }
 
        if (onArc && (pathCommand == null || !pathCommand.isScheduled())) {
           runOrbitMode(pose);
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
     * Builds a PathPlanner on-the-fly path from the robot's current pose
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
        Translation2d toRobot = pose.getTranslation().minus(HUB_CENTRE);
        double currentAngle = Math.atan2(toRobot.getY(), toRobot.getX()); // radians
 
        // --- 2. Joystick → tangential velocity ---
        double rawAxis = MathUtil.applyDeadband(orbitAxisSupplier.getAsDouble(),
                                                JOYSTICK_DEADBAND);
        double tangentialSpeed = rawAxis * MAX_ARC_SPEED; // m/s along arc
 
        // --- 3. Ideal position: keep robot ON the arc ---
        //    We re-project the robot onto the circle so drift is corrected
        //    even without a full path replanner.
        Translation2d idealPos = new Translation2d(
                HUB_CENTRE.getX() + ORBIT_RADIUS * Math.cos(currentAngle),
                HUB_CENTRE.getY() + ORBIT_RADIUS * Math.sin(currentAngle)
        );
 
        // Radial correction: nudge robot toward ideal position
        Translation2d radialError = idealPos.minus(pose.getTranslation());
        // Gentle proportional correction — tune kR as needed
        double kR = 2.0;
        double vxCorrect = radialError.getX() * kR;
        double vyCorrect = radialError.getY() * kR;
 
        // --- 4. Tangent direction at current angle ---
        //    The unit tangent (counter-clockwise positive) is (-sin θ, +cos θ).
        //    Negate it to make positive joystick = clockwise (towards alliance wall).
        double tangentX = -Math.sin(currentAngle);
        double tangentY =  Math.cos(currentAngle);
 
        double vxField = tangentX * tangentialSpeed + vxCorrect;
        double vyField = tangentY * tangentialSpeed + vyCorrect;
 
        // --- 5. Heading PID — always face hub ---
        double desiredHeadingRad = headingToHub(pose.getTranslation()).getRadians();
        double currentHeadingRad = pose.getRotation().getRadians();
        double omega = headingPID.calculate(currentHeadingRad, desiredHeadingRad);
        omega = MathUtil.clamp(omega, -MAX_HEADING_RATE, MAX_HEADING_RATE);
 
        // --- 6. Send to drive ---
        //    fromFieldRelativeSpeeds converts field-frame vx/vy to robot-frame.
        ChassisSpeeds speeds = ChassisSpeeds.fromFieldRelativeSpeeds(
                vxField, vyField, omega, pose.getRotation()
        );
        drive.runVelocity(speeds);
    }
 
 
    /**
     * Returns the point on the orbit semicircle closest to {@code robotPos},
     * clamped to the alliance-legal half of the circle.
     *
     * The semicircle is defined as the arc on the robot's alliance side of
     * the hub.  Adjust the clamp bounds (minAngle / maxAngle) to your field.
     */
    private Translation2d closestArcPoint(Translation2d robotPos) {
        Translation2d toRobot = robotPos.minus(HUB_CENTRE);
        double angle = Math.atan2(toRobot.getY(), toRobot.getX());
 
        // Clamp to the alliance semicircle.
        // Red alliance: hub is roughly at field centre; the alliance side is
        // the right half → angles in [-π/2, +π/2].
        // Blue alliance: left half → angles in [π/2, 3π/2] (or [-π, -π/2] ∪ [π/2, π]).
        // Adjust these bounds to your actual field and hub position.
        if (isRedAlliance) {
            angle = MathUtil.clamp(angle, -Math.PI / 2.0, Math.PI / 2.0);
        } else {
            // Clamp to [π/2, π] ∪ [-π, -π/2] — keep angle on left half
            if (angle > 0) angle = MathUtil.clamp(angle, Math.PI / 2.0, Math.PI);
            else           angle = MathUtil.clamp(angle, -Math.PI, -Math.PI / 2.0);
        }
 
        return new Translation2d(
                HUB_CENTRE.getX() + ORBIT_RADIUS * Math.cos(angle),
                HUB_CENTRE.getY() + ORBIT_RADIUS * Math.sin(angle)
        );
    }
 
    /**
     * Returns the rotation that makes the robot face the hub from {@code pos}.
     */
    private Rotation2d headingToHub(Translation2d pos) {
        Translation2d toHub = HUB_CENTRE.minus(pos);
        return new Rotation2d(Math.atan2(toHub.getY(), toHub.getX()));
    }
}