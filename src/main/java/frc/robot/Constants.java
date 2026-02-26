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

import com.ctre.phoenix6.CANBus;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.util.RobotConfigLoader;

/**
 * This class defines the runtime mode used by AdvantageKit and loads robot-specific configuration
 * based on the roboRIO serial number. The mode is always "real" when running on a roboRIO. Change
 * the value of "simMode" to switch between "sim" (physics sim) and "replay" (log replay from a
 * file).
 */
public final class Constants {
  public static final Mode simMode = Mode.SIM;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  // Robot identification
  public static final String ROBOT_SERIAL_NUMBER;

  // Vision Constants (loaded from config)

  static {
    // Load configuration based on roboRIO serial number (auto-loads on first access)
    ROBOT_SERIAL_NUMBER = RobotConfigLoader.getSerialNumber();
  }

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,
    
    /** Replaying from a log file. */
    REPLAY
  }
  
  public static final int CONTROLLER_PORT_DRIVER = 0;
  public static final int CONTROLLER_PORT_CODRIVER = 1;

  public static final int INTAKE_MOTOR_CAN_ID = 40;
  public static final int DEPLOY_MOTOR_CAN_ID = 41;
  public static final int SHOOTER_MOTOR_CAN_ID = 30;
  public static final int DIVIDER_MOTOR_CAN_ID = 31;
  public static final int CTRLED_CAN_ID = 7;
  
  public static final double SHOOTER_MOTOR_VOLTAGE = 8.0;
  public static final double INTAKE_MOTOR_VOLTAGE = 10.0;
  public static final double DIVIDER_MOTOR_VOLTAGE = 1;
  public static final double DEPLOY_MOTOR_VOLTAGE  = 2.0;
  public static final double RETRACT_MOTOR_VOLTAGE = -2.0;

  public static final String MECH_CANBUS_NAME = "rio";
  public static final CANBus DRIVE_CANIVORE = new CANBus("CANivore");
  
  public static final double DEPLOY_CURRENT_LIMIT = 60;
  public static final double INTAKE_CURRENT_LIMIT = 60;

  public static final Pose2d RED_SHOOT_CENTER = new Pose2d(14.025, 4.029, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d RED_SHOOT_LEFT = new Pose2d(14.021, 2.560, new Rotation2d(Math.toRadians(315)));
  public static final Pose2d RED_SHOOT_RIGHT = new Pose2d(14.023, 5.604, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d RED_TRENCH_LEFT_OUTSIDE = new Pose2d(10.461, 0.814, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d RED_TRENCH_LEFT_INSIDE = new Pose2d(13.508, 0.536, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d RED_TRENCH_RIGHT_OUTSIDE = new Pose2d(10.461, 7.371, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d RED_TRENCH_RIGHT_INSIDE = new Pose2d(13.508, 7.369, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d RED_BUMP_LEFT_OUTSIDE = new Pose2d(10.811, 2.18, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d RED_BUMP_LEFT_INSIDE = new Pose2d(12.8, 2.18, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d RED_BUMP_RIGHT_OUTSIDE = new Pose2d(10.758, 5.5, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d RED_BUMP_RIGHT_INSIDE = new Pose2d(12.8, 5.5, new Rotation2d(Math.toRadians(45)));

  public static final Pose2d BLUE_SHOOT_CENTER = new Pose2d(3.345, 4.029, new Rotation2d(Math.toRadians(180)));
  public static final Pose2d BLUE_SHOOT_RIGHT = new Pose2d(3.349, 2.560, new Rotation2d(Math.toRadians(225))); //TODO: TWEAK COORDINATES
  public static final Pose2d BLUE_SHOOT_LEFT = new Pose2d(3.347, 5.604, new Rotation2d(Math.toRadians(135))); //TODO: TWEAK COORDINATES
  public static final Pose2d BLUE_TRENCH_LEFT_INSIDE = new Pose2d(3.171, 7.371, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d BLUE_TRENCH_LEFT_OUTSIDE = new Pose2d(6.218, 7.371, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d BLUE_TRENCH_RIGHT_INSIDE = new Pose2d(3.171, 0.814, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d BLUE_TRENCH_RIGHT_OUTSIDE = new Pose2d(6.218, 0.814, new Rotation2d(Math.toRadians(0)));
  public static final Pose2d BLUE_BUMP_RIGHT_INSIDE = new Pose2d(3.521, 2.18, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d BLUE_BUMP_LEFT_INSIDE = new Pose2d(3.468, 5.5, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d BLUE_BUMP_LEFT_OUTSIDE = new Pose2d(5.51, 5.5, new Rotation2d(Math.toRadians(45)));
  public static final Pose2d BLUE_BUMP_RIGHT_OUTSIDE = new Pose2d(5.51, 2.18, new Rotation2d(Math.toRadians(45)));

  public static final int SHOOT_RANGE_MAX = 100; // for logic, tweak later
  public static final int SHOOT_RANGE_MIN = 50; // for logic, tweak later
  public static final int SHOOT_ANG_RANGE = 15; // for logic, tweak later
  
  
  public static final Translation2d RED_HUB = new Translation2d(468.56, 158.32);
  public static final Translation2d BLUE_HUB = new Translation2d(181.56, 158.32);


  
}
