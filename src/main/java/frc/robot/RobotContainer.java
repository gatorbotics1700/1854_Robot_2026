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

import java.util.Optional;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.IntakeFuelCommand;
import frc.robot.commands.IntakePivotCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.drive.DriveSubsystem;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOTalonFX;
import frc.robot.subsystems.fuel.FuelSubsystem;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.vision.VisionSubsystem;
import frc.robot.subsystems.vision.VisionConstants;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
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
  private final DriveSubsystem drive;
  private final VisionSubsystem vision;
  private final IntakeSubsystem intake = new IntakeSubsystem();
  private final FuelSubsystem fuel = new FuelSubsystem(); 
  private PathConstraints constraints = new PathConstraints(3.0,5.0, Units.degreesToRadians(540), Units.degreesToRadians(720));
  
    // Controllers
    private final CommandXboxController controller;
    private final CommandXboxController controller_two;
  
    // Dashboard inputs
    private final LoggedDashboardChooser<Command> autoChooser;
  
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
      // Set up robot depending on mode
      switch (Constants.currentMode) {
        case REAL:
          // Real robot, instantiate hardware IO implementations
          controller = new CommandXboxController(Constants.CONTROLLER_PORT_DRIVER);
          controller_two = new CommandXboxController(Constants.CONTROLLER_PORT_CODRIVER);
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
          String osName = System.getProperty("os.name");
          if (osName.contains("Mac")) {
            controller = new CommandSimMacXboxController(Constants.CONTROLLER_PORT_DRIVER);
            controller_two = new CommandSimMacXboxController(Constants.CONTROLLER_PORT_CODRIVER);
          } else {
            controller = new CommandXboxController(Constants.CONTROLLER_PORT_DRIVER);
            controller_two = new CommandXboxController(Constants.CONTROLLER_PORT_CODRIVER);
          }
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
  
        default: // TODO: should the default be real as a safety for matches? to be discussed
          // Replayed robot, disable IO implementations
          controller = new CommandXboxController(Constants.CONTROLLER_PORT_DRIVER);
          controller_two = new CommandXboxController(Constants.CONTROLLER_PORT_CODRIVER);
          drive =
              new DriveSubsystem(
                  new GyroIO() {},
                  new ModuleIO() {},
                  new ModuleIO() {},
                  new ModuleIO() {},
                  new ModuleIO() {},
                  (pose) -> {});
          vision = new VisionSubsystem(drive);
          break;
      }
  
      // Set up auto routines
      autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());
  
      // Set up SysId routines
      // autoChooser.addOption(
      //     "Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
      // autoChooser.addOption(
      //     "Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
      // autoChooser.addOption(
      //     "Drive SysId (Quasistatic Forward)",
      //     drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
      // autoChooser.addOption(
      //     "Drive SysId (Quasistatic Reverse)",
      //     drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
      // autoChooser.addOption(
      //     "Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
      // autoChooser.addOption(
      //     "Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));
  
      // Configure the button bindings
      configureButtonBindings();
    }
  
    /**
     * Use this method to define your button->command mappings. Buttons can be created by
     * instantiating a {@link GenericHID} or one of its subclasses ({@link
     * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
     * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    public void configureButtonBindings() {
      // Default command, normal field-relative drive
      Trigger driverControl =
          new Trigger(
              () ->
                  Math.abs(controller.getLeftY()) > 0.1
                      || Math.abs(controller.getLeftX()) > 0.1
                      || Math.abs(controller.getRightX()) > 0.1);
      var alliance = getAlliance();
      if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red) {
        driverControl
            .whileTrue(
                DriveCommands.joystickDrive(
                    drive,
                    () -> modifyJoystickAxis(controller.getLeftY()), // Changed to raw values
                    () -> modifyJoystickAxis(controller.getLeftX()), // Changed to raw values
                    () -> modifyJoystickAxis(-controller.getRightX()))) // Changed to raw values
            .onFalse(DriveCommands.stopDriveCommand(drive));
      } else if (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Blue) {
        driverControl
            .whileTrue(
                DriveCommands.joystickDrive(
                    drive,
                    () -> modifyJoystickAxis(-controller.getLeftY()), // Changed to raw values
                    () -> modifyJoystickAxis(-controller.getLeftX()), // Changed to raw values
                    () -> modifyJoystickAxis(-controller.getRightX()))) // Changed to raw values
            .onFalse(DriveCommands.stopDriveCommand(drive));
      }
  
      // Lock to 0° when A button is held
      controller
          .a()
          .whileTrue(
              DriveCommands.joystickDriveAtAngle(
                  drive,
                  () -> -controller.getLeftY(),
                  () -> -controller.getLeftX(),
                  () -> new Rotation2d()));
  
      // Reset gyro to 0° when B button is pressed
      controller
          .b()
          .onTrue(
              Commands.runOnce(
                      () -> {
                        if (DriverStation.getAlliance().isPresent()
                            && DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
                          drive.setPose(
                              new Pose2d(
                                  drive.getPose().getTranslation(),
                                  new Rotation2d(Math.toRadians(0))));
                        } else {
                          drive.setPose(
                              new Pose2d(
                                  drive.getPose().getTranslation(),
                                  new Rotation2d(Math.toRadians(0))));
                        }
                      },
                      drive)
                  .ignoringDisable(true));
  
      controller_two            
          .b()
          .onTrue(
            new ShootCommand(fuel, Constants.SHOOTER_MOTOR_VOLTAGE, Constants.DIVIDER_MOTOR_VOLTAGE)
            ); 

      controller_two
          .a()
          .onTrue(
            new IntakeFuelCommand(intake, Constants.INTAKE_MOTOR_VOLTAGE)
          );

      controller_two
          .x()
          .onTrue(
            new ShootCommand(fuel,0, 0)
          );



      controller_two
          .y()
          .onTrue(
            Commands.runOnce(
              () -> {getIntakeCommand(intake).execute();}
            )
          );
        
    // Lock to 0° when A button is held
    controller
        .a()
        .whileTrue(
            DriveCommands.joystickDriveAtAngle(
                drive,
                () -> -controller.getLeftY(),
                () -> -controller.getLeftX(),
                () -> new Rotation2d()));
  

    // Reset gyro to 0° when B button is pressed
    controller
        .b()
        .onTrue(
            Commands.runOnce(
                    () -> {
                      if (DriverStation.getAlliance().isPresent()
                          && DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
                        drive.setPose(
                            new Pose2d(
                                drive.getPose().getTranslation(),
                                new Rotation2d(Math.toRadians(0))));
                      } else {
                        drive.setPose(
                            new Pose2d(
                                drive.getPose().getTranslation(),
                                new Rotation2d(Math.toRadians(0))));
                      }
                    },
                    drive)
                .ignoringDisable(true));

      controller
        .rightBumper()
        .onTrue(
          Commands.runOnce(
            () -> {
              if (isInAllianceZone() == true) {
              AutoBuilder.pathfindToPose(Constants.BUMP_RIGHT_INSIDE, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.BUMP_RIGHT, constraints, 0.0)).schedule(); 
            } else {
              AutoBuilder.pathfindToPose(Constants.BUMP_RIGHT, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.BUMP_RIGHT_INSIDE, constraints, 0.0)).schedule();
            }}
          ));
      controller
        .leftBumper()
        .onTrue(
          Commands.runOnce(
            () -> {
              if (isInAllianceZone() == true) {
              AutoBuilder.pathfindToPose(Constants.BUMP_LEFT_INSIDE, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.BUMP_LEFT, constraints, 0.0)).schedule(); 
            } else {
              AutoBuilder.pathfindToPose(Constants.BUMP_LEFT, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.BUMP_LEFT_INSIDE, constraints, 0.0)).schedule();
            }}
          ));

      controller
        .rightTrigger()
        .onTrue(
          Commands.runOnce(
            () -> {
              if (isInAllianceZone() == true) {
              AutoBuilder.pathfindToPose(Constants.TRENCH_RIGHT_INSIDE, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.TRENCH_RIGHT, constraints, 0.0)).schedule(); 
            } else {
              AutoBuilder.pathfindToPose(Constants.TRENCH_RIGHT, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.TRENCH_RIGHT_INSIDE, constraints, 0.0)).schedule();
            }}
        ));
      controller
          .leftTrigger()
          .onTrue(
            Commands.runOnce(
            () -> {
              if (isInAllianceZone() == true) {
              AutoBuilder.pathfindToPose(Constants.TRENCH_LEFT_INSIDE, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.TRENCH_LEFT, constraints, 0.0)).schedule(); 
            } else {
              AutoBuilder.pathfindToPose(Constants.TRENCH_LEFT, constraints, constraints.maxVelocity()).andThen(AutoBuilder.pathfindToPose(Constants.TRENCH_LEFT_INSIDE, constraints, 0.0)).schedule();
            }}
        ));

      controller
        .povLeft()
        .onTrue(
            AutoBuilder.pathfindToPose(Constants.SHOOT_LEFT, constraints, 0.0)); 
      
      controller
        .povUp()
        .onTrue(
            AutoBuilder.pathfindToPose(Constants.SHOOT_CENTER, constraints, 0.0)); 
      
      controller
        .povRight()
        .onTrue(
            AutoBuilder.pathfindToPose(Constants.SHOOT_RIGHT, constraints, 0.0)); 
  }
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    try {
      return autoChooser.get();
    } catch (Exception ioe) {
      System.out.println("bad io error");
      return Commands.none();
    }
  }

  public Command getIntakeCommand(IntakeSubsystem intake) {
    if (intake.isDeployed() == true){
              return new IntakePivotCommand(intake, Constants.RETRACT_MOTOR_POSITION);
            } else {
              return new IntakePivotCommand(intake,Constants.DEPLOY_MOTOR_POSITION);
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
    if (DriverStation.getAlliance().get() == DriverStation.Alliance.Red) {
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

  private double modifyJoystickAxis(double value) {
    // Deadband
    value = deadband(value, 0.025);

    // Square the axis
    value =
        Math.copySign(
            RobotConfigLoader.getDouble("container.joystick_scale_factor") * Math.pow(value, 2),
            value);

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

    // Log command information with names
    Command driveCmd = drive.getCurrentCommand();

    //double shooterMotorVoltage = fuel.getShooterMotorVoltage(Constants.currentMode); see if this could be fixed

    Logger.recordOutput("Commands/DriveCommand", driveCmd != null ? driveCmd.getName() : "None");
    Logger.recordOutput("Commands/ShooterVoltage", fuel.getShooterMotorVoltage(Constants.currentMode));
    Logger.recordOutput("Commands/IntakeVoltage", intake.getIntakeMotorVoltage(Constants.currentMode));
    Logger.recordOutput("Commands/IntakeState", intake.isDeployed());
    //Logger.recordOutput("Commands/shooterVoltage", shooterMotorVoltage); see if thsi can be fixed

    // Log if commands are running
    Logger.recordOutput("Commands/DriveCommandActive", driveCmd != null);
  }

  private Optional<Alliance> getAlliance() {
    switch (Constants.currentMode) {
      case REAL:
        return DriverStation.getAlliance();
      case SIM: // default to blue in sim
        return Optional.of(DriverStation.getAlliance().orElse(DriverStation.Alliance.Blue));
      default:
        return DriverStation.getAlliance();
     }
  }
}
