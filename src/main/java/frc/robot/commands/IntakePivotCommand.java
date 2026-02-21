package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.intake.IntakeSubsystem;
 

public class IntakePivotCommand extends Command {
    
    private final IntakeSubsystem intakeSubsystem;
    private double voltage;
    private long startTime;


    public IntakePivotCommand (IntakeSubsystem intakeSubsystem, double voltage){
        this.intakeSubsystem = intakeSubsystem;
        this.voltage = voltage;
        startTime = System.currentTimeMillis();
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        if (voltage == Constants.DEPLOY_MOTOR_VOLTAGE) {
            System.out.println("DEPLOYING");
            intakeSubsystem.setIsDeployed(true);
        }
        else if (voltage == Constants.RETRACT_MOTOR_VOLTAGE) {
            System.out.println("RETRACTING");
            intakeSubsystem.setIsDeployed(false);
        }
        intakeSubsystem.setDeployMotorVoltage(voltage);
    }

    @Override 
    public boolean isFinished () {
        if (intakeSubsystem.getDeployMotorCurrent() >= Constants.DEPLOY_CURRENT_LIMIT - 10) { 
            intakeSubsystem.setDeployMotorVoltage(0);
            return true;

        }
        else if (( System.currentTimeMillis() - startTime) > 1000) {
            intakeSubsystem.setDeployMotorVoltage(0);
            return true;
        } 
        return false;
    }
}