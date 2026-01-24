package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;
 

public class IntakePivotCommand extends Command {
    
    private final Intake intakeSubsystem;
    private double position;
    private long startTime;

    public IntakePivotCommand (Intake intakeSubsystem, double position){
        this.intakeSubsystem = intakeSubsystem;
        this.position = position;
        startTime = System.currentTimeMillis();
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        if (position == Constants.DEPLOY_MOTOR_POSITION) {
            System.out.println("DEPLOYING");
        }
        else if (position == Constants.RETRACT_MOTOR_POSITION) {
            System.out.println("RETRACTING");
        }
        intakeSubsystem.moveDeployMotor(position);
    }

    @Override 
    public boolean isFinished () {
        if (Math.abs(intakeSubsystem.getDeployMotorPosition() - position) < Constants.PIVOT_DEADBAND) { 
            return true;
        }
        else if (( System.currentTimeMillis() - startTime) > 3000) {
            return true;
        }
        return false;
    }
}