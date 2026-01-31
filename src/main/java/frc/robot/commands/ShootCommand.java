package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.fuel.FuelSubsystem;

public class ShootCommand extends Command{

    private FuelSubsystem fuelSubsystem;
    private double shooterVoltage;
    private double dividerVoltage;
    
    public ShootCommand(FuelSubsystem fuelSubsystem, double shooterVoltage, double dividerVoltage){
        this.fuelSubsystem = fuelSubsystem; 
        this.shooterVoltage = shooterVoltage;
        this.dividerVoltage = dividerVoltage;
        System.out.println("shoot has been shot");
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        if (shooterVoltage != 0) {
            System.out.println("SHOOTING");

        }
        

        fuelSubsystem.moveDividerMotor(dividerVoltage);
        fuelSubsystem.moveShooterMotor(shooterVoltage);
    }

    @Override
    public boolean isFinished(){
        if (shooterVoltage == fuelSubsystem.getShooterMotorVoltage(Constants.currentMode)) {
            System.out.println("LEBRON IS FINISHED");
            return true; 
        }
        
        return false;
        // TODO: should we check what type of condition needs to be met
    }
}



