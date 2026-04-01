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
            intakeSubsystem.moveIntakeMotor(Constants.INTAKE_MOTOR_VOLTAGE);
        }
        else if (voltage == Constants.RETRACT_MOTOR_VOLTAGE) {
            System.out.println("RETRACTING");
            intakeSubsystem.setIsDeployed(false);
            intakeSubsystem.moveIntakeMotor(0);
        }
        else{
            System.out.println("lebron unknown pivot voltage");
        }
        intakeSubsystem.setDeployMotorVoltage(voltage);
    }

    @Override 
    public boolean isFinished () {
        if (intakeSubsystem.getDeployMotorCurrent(Constants.currentMode) >= Constants.DEPLOY_CURRENT_LIMIT - 10) { 
            if (voltage == Constants.DEPLOY_MOTOR_VOLTAGE){
                intakeSubsystem.setDeployMotorVoltage(Constants.DEPLOY_REST_VOLTAGE);
            } else {
                intakeSubsystem.setDeployMotorVoltage(0);
            }
            System.out.println("lebron hit something, hit current limit");
            return true;
        }
        else if (( System.currentTimeMillis() - startTime) > 750) {
            intakeSubsystem.setDeployMotorVoltage(0);
            System.out.println("lebron timed out");
            return true;
        } 
        return false;
    }
}