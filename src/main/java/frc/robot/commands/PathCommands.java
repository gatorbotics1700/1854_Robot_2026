package frc.robot.commands;


import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;


public class PathCommands {
    //right bump
    public static Command driveRightBump(boolean isInAllianceZone, Alliance alliance, PathConstraints constraints) {
        return Commands.runOnce(
            () -> {
              if (alliance == DriverStation.Alliance.Red) {
                if (isInAllianceZone == true) {
                  AutoBuilder.pathfindToPose(Constants.RED_BUMP_RIGHT_INSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.RED_BUMP_RIGHT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                  AutoBuilder.pathfindToPose(Constants.RED_BUMP_RIGHT_OUTSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.RED_BUMP_RIGHT_INSIDE, constraints, 0.0)).schedule();
                }
              } else {
                if (isInAllianceZone  == true) {
                  AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_RIGHT_INSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_RIGHT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                  AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_RIGHT_OUTSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_RIGHT_INSIDE, constraints, 0.0)).schedule();
                }
              }
            }
        );
    }

    public static Command driveLeftBump(boolean isInAllianceZone, Alliance alliance, PathConstraints constraints) {
        return Commands.runOnce(
            () -> {
              if (alliance == DriverStation.Alliance.Red) {
                if (isInAllianceZone == true) {
                  AutoBuilder.pathfindToPose(Constants.RED_BUMP_LEFT_INSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.RED_BUMP_LEFT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                  AutoBuilder.pathfindToPose(Constants.RED_BUMP_LEFT_OUTSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.RED_BUMP_LEFT_INSIDE, constraints, 0.0)).schedule();
                }
              } else {
                if (isInAllianceZone == true) {
                  AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_LEFT_INSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_LEFT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                  AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_LEFT_OUTSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_BUMP_LEFT_INSIDE, constraints, 0.0)).schedule();
                }
              }
            }
        );
    }
    public static Command driveRightTrench(boolean isInAllianceZone, Alliance alliance, PathConstraints constraints) {
        return Commands.runOnce(
            () -> {
              if (alliance == DriverStation.Alliance.Red) {
                if (isInAllianceZone == true) {
                AutoBuilder.pathfindToPose(Constants.RED_TRENCH_RIGHT_INSIDE, constraints, constraints.maxVelocity())
                .andThen(AutoBuilder.pathfindToPose(Constants.RED_TRENCH_RIGHT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                AutoBuilder.pathfindToPose(Constants.RED_TRENCH_RIGHT_OUTSIDE, constraints, constraints.maxVelocity())
                .andThen(AutoBuilder.pathfindToPose(Constants.RED_TRENCH_RIGHT_INSIDE, constraints, 0.0)).schedule();
              }
              } else {
                if (isInAllianceZone == true) {
                  AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_RIGHT_INSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_RIGHT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                  AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_RIGHT_OUTSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_RIGHT_INSIDE, constraints, 0.0)).schedule();
                }
              }
            }   
          );
    }

    public static Command driveLeftTrench(boolean isInAllianceZone, Alliance alliance, PathConstraints constraints) {
          return Commands.runOnce(
            () -> {
              if (alliance == DriverStation.Alliance.Red) {
                if (isInAllianceZone == true) {
                AutoBuilder.pathfindToPose(Constants.RED_TRENCH_LEFT_INSIDE, constraints, constraints.maxVelocity())
                .andThen(AutoBuilder.pathfindToPose(Constants.RED_TRENCH_LEFT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                AutoBuilder.pathfindToPose(Constants.RED_TRENCH_LEFT_OUTSIDE, constraints, constraints.maxVelocity())
                .andThen(AutoBuilder.pathfindToPose(Constants.RED_TRENCH_LEFT_INSIDE, constraints, 0.0)).schedule();
              }
              } else {
                if (isInAllianceZone == true) {
                  AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_LEFT_INSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_LEFT_OUTSIDE, constraints, 0.0)).schedule(); 
                } else {
                  AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_LEFT_OUTSIDE, constraints, constraints.maxVelocity())
                  .andThen(AutoBuilder.pathfindToPose(Constants.BLUE_TRENCH_LEFT_INSIDE, constraints, 0.0)).schedule();
                }
              }
            }   
          );
    }
    public static Command driveShootLeft(Alliance alliance, PathConstraints constraints) {
          return Commands.runOnce(
            () -> {
              System.out.println("ALLIANCE " + alliance);
              if (alliance == DriverStation.Alliance.Red) {
                AutoBuilder.pathfindToPose(Constants.RED_SHOOT_LEFT, constraints,0.0).schedule();
              } else {
                AutoBuilder.pathfindToPose(Constants.BLUE_SHOOT_LEFT, constraints, 0.0).schedule();
              }}   
          );
    }
    public static Command driveShootCenter(Alliance alliance, PathConstraints constraints) {
          return Commands.runOnce(
            () -> {
              if (alliance == DriverStation.Alliance.Red) {
                AutoBuilder.pathfindToPose(Constants.RED_SHOOT_CENTER, constraints,0.0).schedule();
              } else {
                AutoBuilder.pathfindToPose(Constants.BLUE_SHOOT_CENTER, constraints, 0.0).schedule();
              }}   
          );
    }
    public static Command driveShootRight(Alliance alliance, PathConstraints constraints) {
          return Commands.runOnce(
            () -> {
              System.out.println("ALLIANCE " + alliance);
              if (alliance == DriverStation.Alliance.Red) {
                AutoBuilder.pathfindToPose(Constants.RED_SHOOT_RIGHT, constraints,0.0).schedule();
              } else {
                AutoBuilder.pathfindToPose(Constants.BLUE_SHOOT_RIGHT, constraints, 0.0).schedule();
              }}   
          );
    }

    


}
