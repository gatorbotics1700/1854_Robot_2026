// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import java.util.ArrayList;
import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.ctre.phoenix6.hardware.CANdle;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.hal.util.AllocationException;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.IntakeFuelCommand;
import frc.robot.commands.IntakePivotCommand;
import frc.robot.commands.PathCommands;
import frc.robot.commands.ShootCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drive.DriveSubsystem;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.led.LedSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.subsystems.vision.VisionConstants;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
import frc.robot.subsystems.vision.VisionSubsystem;
import frc.robot.util.CommandSimMacXboxController;
import frc.robot.util.RobotConfigLoader;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems 
  private DriveSubsystem drive;
  private VisionSubsystem vision;
  private IntakeSubsystem intake = new IntakeSubsystem();
  private ShooterSubsystem fuel = new ShooterSubsystem();
  private LedSubsystem led = new LedSubsystem();
  
  private PathConstraints constraints = new PathConstraints(3.0,5.0, Units.degreesToRadians(540), Units.degreesToRadians(720));
  
    // Controllers
    private CommandXboxController controller;
    private CommandXboxController controller_two;
  
    // Dashboard inputs
    private final LoggedDashboardChooser<Command> autoChooser;

    Command shootCommand = new ShootCommand(fuel, Constants.SHOOTER_MOTOR_VOLTAGE, Constants.DIVIDER_MOTOR_VOLTAGE);
    Command runIntakeFuelCommand = new IntakeFuelCommand(intake, Constants.INTAKE_MOTOR_VOLTAGE,Constants.currentMode);
    Command stopShootCommand = new ShootCommand(fuel,Constants.SHOOTER_MOTOR_VOLTAGE, 0);
    Command fullStopShootCommand = new ShootCommand(fuel, 0, 0);
    Command stopIntakeCommand = new IntakeFuelCommand(intake, 0.0,Constants.currentMode);
    Command deployCommand = new IntakePivotCommand(intake,Constants.DEPLOY_MOTOR_VOLTAGE);
    Command rightShootCommand = PathCommands.driveShootRight(getAlliance().get(),constraints);

     public void setupControllers(){
        switch (Constants.currentMode) {
        case REAL:
          // Real robot, instantiate hardware IO implementations
          controller = new CommandXboxController(Constants.CONTROLLER_PORT_DRIVER);
          controller_two = new CommandXboxController(Constants.CONTROLLER_PORT_CODRIVER);
          break;
         case SIM:
          String osName = System.getProperty("os.name");
          if (osName.contains("Mac")) {
            controller = new CommandSimMacXboxController(Constants.CONTROLLER_PORT_DRIVER);
            controller_two = new CommandSimMacXboxController(Constants.CONTROLLER_PORT_CODRIVER);
          } else {
            controller = new CommandXboxController(Constants.CONTROLLER_PORT_DRIVER);
            controller_two = new CommandXboxController(Constants.CONTROLLER_PORT_CODRIVER);}
          break;
          default:
          // Replayed robot, disable IO implementations
            controller = new CommandXboxController(Constants.CONTROLLER_PORT_DRIVER);
            controller_two = new CommandXboxController(Constants.CONTROLLER_PORT_CODRIVER);
      }
    }
     
    public void setupSubsystems(){
        switch (Constants.currentMode) {
        case REAL:
          // Real robot, instantiate hardware IO implementations
          drive =
              new DriveSubsystem(
                  new GyroIOPigeon2(),
                  new ModuleIOTalonFX(TunerConstants.FrontLeft),
                  new ModuleIOTalonFX(TunerConstants.FrontRight),
                  new ModuleIOTalonFX(TunerConstants.BackLeft),
                  new ModuleIOTalonFX(TunerConstants.BackRight),
                  (pose) -> {});
    
          vision =
              new VisionSubsystem(
                  drive,
                  new VisionIOLimelight(
                      VisionConstants.LIMELIGHT_0_NAME,
                      drive::getRotation,
                      VisionConstants.ROBOT_TO_LIMELIGHT_0));
  
          break;

        case SIM:
          // Sim robot, instantiate physics sim IO implementations
          drive =
              new DriveSubsystem(
                  new GyroIO() {},
                  new ModuleIOSim(TunerConstants.FrontLeft),
                  new ModuleIOSim(TunerConstants.FrontRight),
                  new ModuleIOSim(TunerConstants.BackLeft),
                  new ModuleIOSim(TunerConstants.BackRight),
                  (pose) -> {});
          vision =
              new VisionSubsystem(
                  drive,
                  new VisionIOPhotonVisionSim(
                      VisionConstants.LIMELIGHT_0_NAME,
                      VisionConstants.ROBOT_TO_LIMELIGHT_0,
                      drive::getPose));
          DriverStation.silenceJoystickConnectionWarning(true);
          break;
  
        default:
          // Replayed robot, disable IO implementations
          drive =
              new DriveSubsystem(
                  new GyroIO() {},
                  new ModuleIO() {},
                  new ModuleIO() {},
                  new ModuleIO() {},
                  new ModuleIO() {},
                  (pose) -> {});
          vision = new VisionSubsystem(drive);
          break;}
         }


    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    
      // Set up robot depending on mode
     

    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */

    public void configureButtonBindings() {
      CommandScheduler.getInstance().getActiveButtonLoop().clear();

      // Default command, normal field-relative drive
      Trigger driverControl =
          new Trigger(
              () ->
                  Math.abs(controller.getLeftY()) > 0.1
                      || Math.abs(controller.getLeftX()) > 0.1
                      || Math.abs(controller.getRightY()) > 0.1
                      || Math.abs(controller.getRightX()) > 0.1);
      var alliance = getAlliance();
      Trigger ifAllianceChanged = 
          new Trigger(
            () ->
                  getAlliance() != alliance)
                  .onChange(
                    Commands.runOnce(() -> {
                      System.out.println("Lebron changed teams");
                      updateLEDsAndAcceptVisionPose();
                    },
                    led
                  ).andThen(
                    Commands.runOnce(() -> {
                      configureButtonBindings(); 
                    })
                  )
                  .ignoringDisable(true));

      if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {
        driverControl
            .whileTrue(
                  DriveCommands.joystickDriveAtAngle(
                    drive,
                    () -> modifyJoystickAxis(-controller.getLeftY(),false), // Changed to raw values
                    () -> modifyJoystickAxis(-controller.getLeftX(),false), // Changed to raw values
                    () -> getJoystickAngle(controller.getRightX(),-controller.getRightY(), drive.getRotation()),  // Changed to raw values
                    getAlliance()))
            .onFalse(DriveCommands.stopDriveCommand(drive));
      } else { // blue = default when no alliance
        driverControl
            .whileTrue(
                DriveCommands.joystickDriveAtAngle(
                    drive,
                    () -> modifyJoystickAxis(-controller.getLeftY(),false), // Changed to raw values
                    () -> modifyJoystickAxis(-controller.getLeftX(),false), // Changed to raw values
                    () -> getJoystickAngle(-controller.getRightX(),controller.getRightY(), drive.getRotation()), // Changed to raw values
                    getAlliance()))
            .onFalse(DriveCommands.stopDriveCommand(drive));
      }
      
      controller
        .a()
        .onChange( // Set slow when pressed, undo when released
          Commands.runOnce(
                () -> {
                  drive.setSlowDrive();
                },
                drive));

      controller
          .b()
          .onTrue(
            Commands.runOnce(
                () -> {
                  if(alliance.isPresent()){
                    drive.zeroGyroscope(alliance.get());
                  }
                },
                drive));
  
      controller_two            
          .rightTrigger()
          .onTrue(
            Commands.runOnce(
              () -> {
                // Note: this does work, but it takes a while for the divider motor to (visibly) slow down
                shooterToggleCommand().schedule();
              }
            )
          ); 

      controller_two
          .a()
          .onTrue(
            runIntakeFuelCommand
          );

      controller_two
          .leftTrigger()
          .onTrue(
            Commands.runOnce(
              () -> {
                getIntakePivotCommand().schedule();
              }
            ,
            intake
          )
        );

      controller_two
          .b()
          .onTrue(
            fullStopShootCommand
          );

    // Reset gyro to 0° when B button is pressed
    controller
        .start()
        .onTrue(
            Commands.runOnce(
                    () -> {
                      if (getAlliance().isPresent()
                          && getAlliance().get() == DriverStation.Alliance.Red) {
                        drive.setPose(
                            new Pose2d(
                                drive.getPose().getTranslation(),
                                new Rotation2d(Math.toRadians(0))));
                      } else {
                        drive.setPose(
                            new Pose2d(
                                drive.getPose().getTranslation(),
                                new Rotation2d(Math.toRadians(180))));
                      }
                    },
                    drive)
                .ignoringDisable(true));

      
      controller
        .rightBumper()
        .onTrue(
            Commands.runOnce(
              () -> {    
                if (getAlliance().isPresent() == true) {
                  PathCommands.driveRightBump(isInAllianceZone(), getAlliance().get(), constraints).schedule();
                }
              }
        ));
      controller
        .leftBumper()
        .onTrue(
            Commands.runOnce(
              () -> {    
                if (getAlliance().isPresent() == true) {
                  PathCommands.driveLeftBump(isInAllianceZone(), getAlliance().get(), constraints).schedule();
                }
              }
        ));

      controller
        .rightTrigger()
        .onTrue(
            Commands.runOnce(
              () -> {    
                if (getAlliance().isPresent() == true) {
                  PathCommands.driveRightTrench(isInAllianceZone(), getAlliance().get(), constraints).schedule();
                }
              }
        ));
      controller
        .leftTrigger()
        .onTrue(
            Commands.runOnce(
              () -> {    
                if (getAlliance().isPresent() == true) {
                  PathCommands.driveLeftTrench(isInAllianceZone(), getAlliance().get(), constraints).schedule();
                }
              }
        ));

      controller
        .povLeft()
        .onTrue(
            PathCommands.driveShootLeft(getAlliance().get(), constraints)
          );
      
      controller
        .povUp()                                                                                                                                                                                                                                 
        .onTrue(
            PathCommands.driveShootCenter(getAlliance().get(), constraints)
          ); 
      
      controller
        .povRight()
        .onTrue(
          PathCommands.driveShootRight(getAlliance().get(), constraints)
        );
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */

   public RobotContainer() {
      
      NamedCommands.registerCommand("shootCenter",  shootCommand);
      NamedCommands.registerCommand("runIntake", runIntakeFuelCommand);
      NamedCommands.registerCommand("stopShoot", stopShootCommand);
      NamedCommands.registerCommand("fullStopShoot", fullStopShootCommand);
      NamedCommands.registerCommand("stopIntake", stopIntakeCommand);
      NamedCommands.registerCommand("deployIntake", deployCommand);
      NamedCommands.registerCommand("rightShoot", rightShootCommand);
  
      
      
    
      setupSubsystems();
      setupControllers();
      configureButtonBindings();

      autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());
    }
  public Command getAutonomousCommand() {
    try {
      return autoChooser.get();
    } catch (Exception ioe) {
      System.out.println("bad io error");
      return Commands.none();
    }
  }

  public Command getIntakePivotCommand() {
    if (intake.isDeployed()){
      return new IntakePivotCommand(intake, Constants.RETRACT_MOTOR_VOLTAGE);
    } else {
      return new IntakePivotCommand(intake, Constants.DEPLOY_MOTOR_VOLTAGE);
    }
  }

  public Command shooterToggleCommand() {
    if (fuel.shooterOn()){
      return stopShootCommand;
    } else {
      return shootCommand;
    }
  }

  public DriveSubsystem getDriveSubsystem() {
    return drive;
  }

  private double deadband(double value, double deadband) {
    // If controller reads very tiny value close to zero, we don't want to make the robot think it
    // has to move
    // Without deadband, robot will think it has to move, and then it will go crazy
    if (Math.abs(value) > deadband) {
      if (value > 0.0) {
        return (value - deadband) / (1.0 - deadband);
      } else {
        return (value + deadband) / (1.0 - deadband);
      }
    } else {
      return 0.0; // Return 0 if absolute value is within desired margin of error
    }
  }

  private boolean isInAllianceZone() {
    if (getAlliance().isPresent() && getAlliance().get() == DriverStation.Alliance.Red) {
      if (drive.getPose().getX() > 12.208) {
        return true;
      } else {
        return false;
      }
    } else {
      if (drive.getPose().getX() < 4.386) {
        return true;
      } else {
        return false;
      }
    }

  }

  private double modifyJoystickAxis(double value, boolean isAngle) {
    // Deadband
    value = deadband(value, 0.026);

    // Square the axis
    if (isAngle == false) {
      value =
          Math.copySign(
              RobotConfigLoader.getDouble("container.joystick_scale_factor") * Math.pow(value, 2),
              value);
    }

    if (drive.getSlowDrive()) {
      return 0.5 * value;
    }

    return value;
  }

  /**
   * Periodic method to log button states and other robot information. Call this from
   * Robot.teleopPeriodic() and Robot.autonomousPeriodic().
   */
  public void periodic() {
    // Log button states directly - much simpler!
    Logger.recordOutput("Buttons/Controller1/A", controller.a().getAsBoolean());
    Logger.recordOutput("Buttons/Controller1/B", controller.b().getAsBoolean());
    Logger.recordOutput("Buttons/Controller1/X", controller.x().getAsBoolean());
    Logger.recordOutput("Buttons/Controller1/Y", controller.y().getAsBoolean());

    Logger.recordOutput("Buttons/Controller2/A", controller_two.a().getAsBoolean());
    Logger.recordOutput("Buttons/Controller2/B", controller_two.b().getAsBoolean());
    Logger.recordOutput("Buttons/Controller2/X", controller_two.x().getAsBoolean());
    Logger.recordOutput("Buttons/Controller2/Y", controller_two.y().getAsBoolean());

    // Log command scheduler status
    Logger.recordOutput("Commands/SchedulerActive", true);
    Logger.recordOutput("Commands/LogTime", System.currentTimeMillis());
    Logger.recordOutput("LoggedRobot/CANivoreUtil", Constants.DRIVE_CANIVORE.getStatus().BusUtilization);

    // Log command information with names
    Command driveCmd = drive.getCurrentCommand();

    //double shooterMotorVoltage = fuel.getShooterMotorVoltage(Constants.currentMode); see if this could be fixed

    Logger.recordOutput("Commands/DriveCommand", driveCmd != null ? driveCmd.getName() : "None");
    Logger.recordOutput("Commands/ShooterVoltage", fuel.getShooterMotorVoltage(Constants.currentMode));
    Logger.recordOutput("Commands/DividerVoltage", fuel.getDividerMotorVoltage(Constants.currentMode));
    Logger.recordOutput("Commands/IntakeVoltage", intake.getIntakeMotorVoltage(Constants.currentMode));
    Logger.recordOutput("Commands/IntakeState", intake.isDeployed());
    //Logger.recordOutput("Commands/shooterVoltage", shooterMotorVoltage); see if thsi can be fixed

    // Log if commands are running
    Logger.recordOutput("Commands/DriveCommandActive", driveCmd != null);
    
    updateLEDsAndAcceptVisionPose();
  }

  public void updateLEDsAndAcceptVisionPose() {
    Pose2d pose = drive.getPose();

    // Set the color for the alliance
    if (getAlliance().equals(Optional.of(DriverStation.Alliance.Red))){
      led.setColor(255,0,0);
    } else if (getAlliance().equals(Optional.of(DriverStation.Alliance.Blue))){
      led.setColor(0,0,255);
    } else {
      led.setColor(255,255,0); // yellow = BAD
    }
    led.setSolid();

    // Add some purple if we see an april tag
    if(vision.poseAccepted){
      // We must copy the robot poses. This is to avoid a race condition where 
      // the list grows+shrinks as we loop through it.
      // -- talk to Patricia if you're curious about why this is necessary.
      ArrayList<Pose3d> acceptedPoses = new ArrayList<Pose3d>();
      acceptedPoses.addAll(vision.allRobotPosesAccepted);
      for(int i = 0; i < acceptedPoses.size(); i++){
        Pose2d visionPose = acceptedPoses.get(i).toPose2d();
        drive.accept(visionPose, System.currentTimeMillis());
      }
      if (acceptedPoses.size() > 1) {
        led.setBlinking();
        drive.setPose(drive.poseEstimator.getEstimatedPosition());
      }
    } else{
      led.setSolid();
    }

    if (getAlliance().isEmpty()) {
      led.setColor(255,255,0);
      return;
    } 

    Alliance alliance = getAlliance().get();

    Translation2d target = 
      alliance == Alliance.Blue ? Constants.BLUE_HUB : Constants.RED_HUB;

    double distance = pose.getTranslation().getDistance(target);

    Translation2d robotToTarget = target.minus(pose.getTranslation());

    Rotation2d angleToTarget = robotToTarget.getAngle();

    Rotation2d angleError = angleToTarget.minus(pose.getRotation());

    boolean inDistance =
        distance < Constants.SHOOT_RANGE_MAX &&
        distance > Constants.SHOOT_RANGE_MIN;
    boolean inAngle =
        Math.abs(angleError.getDegrees()) < Constants.SHOOT_ANG_RANGE;

    if (inDistance && inAngle) {
      led.setColor(0,255,0);
    } else {
      if (alliance == DriverStation.Alliance.Blue){
          led.setColor(0,0,255);
      }
      if (alliance == DriverStation.Alliance.Red){
          led.setColor(255,0,0);
      }
    }

  }
  

  private Optional<Alliance> getAlliance() {
    switch (Constants.currentMode) {
      case REAL: 
        
        if (getAutonomousCommand().getName().startsWith("Blue", 0)) {
          return Optional.of((DriverStation.Alliance.Blue));
        } else if (getAutonomousCommand().getName().startsWith("Red",0)) {
          return Optional.of((DriverStation.Alliance.Red));
        } else {
          // If none provided, rely on driver station.
          return DriverStation.getAlliance();
        }
        
      case SIM: // default to blue in sim
        return Optional.of((DriverStation.Alliance.Blue));
      default:
        return DriverStation.getAlliance();
     }
  }

  // Schedule commands to stop the intake, and the shooter
  // Patricia added this for safety -- talk to her before messing with this.
  public void stopAllMotors() {
    fuel.moveDividerMotor(0);
    fuel.moveShooterMotor(0);
    intake.moveIntakeMotor(0);
    intake.setDeployMotorVoltage(0);
  }

  private Rotation2d getJoystickAngle(double x, double y, Rotation2d currentDriveAngle){
    if (Math.abs(x) < .1 && Math.abs(y) < .1) { // deadband; keep the robot at its current angle
      return currentDriveAngle;
    } else {
      double a = modifyJoystickAxis(x, true);
      double b = modifyJoystickAxis(y, true);
      Rotation2d newAngle = new Rotation2d(Math.atan2(a,b));
      return newAngle;
    }
  }
}
