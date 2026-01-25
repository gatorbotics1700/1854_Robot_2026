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

  public static final int KRAKEN_TICKS_PER_REV = 2048;
  public static final double OUTAKE_MOTOR_VOLTAGE = 1.0;

  public static final int FUEL_MOTOR_CAN_ID = 0;
  public static final double INTAKE_MOTOR_VOLTAGE = 4.0;
  public static final int DEPLOY_MOTOR_CAN_ID = 4;
  public static final double SHOOTER_MOTOR_VOLTAGE = 4.0;
  public static final double DIVIDER_MOTOR_VOLTAGE = 1;
    public static final double RELEASE_SHOOTER_MOTOR_VOLTAGE = 4.0;
  public static final double RELEASE_DIVIDER_MOTOR_VOLTAGE = 1;

  public static final int SHOOTER_MOTOR_CAN_ID = 30;
  public static final String MECH_CANBUS_NAME = "";
  public static final int DIVIDER_MOTOR_CAN_ID = 31;
   public static final double DEPLOY_MOTOR_POSITION  = 2;
  public static final double RETRACT_MOTOR_POSITION = 1;
  public static final double PIVOT_DEADBAND = 2;

}
