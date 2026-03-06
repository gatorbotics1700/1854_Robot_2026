package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.fuel.FuelSubsystem;

public class ShootCommand extends Command{

    private FuelSubsystem fuelSubsystem;
    private double shooterVoltage;
    private double dividerVoltage;
    private boolean executed;
    
    public ShootCommand(FuelSubsystem fuelSubsystem, double shooterVoltage, double dividerVoltage){
        this.fuelSubsystem = fuelSubsystem; 
        this.shooterVoltage = shooterVoltage;
        this.dividerVoltage = dividerVoltage;
        executed = false;
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        executed = false;
        if (shooterVoltage != 0) {
            System.out.println("SHOOTING");
        }
        fuelSubsystem.moveShooterMotor(shooterVoltage);
        if (shooterVoltage == 0 || fuelSubsystem.shooterWarmedUp()) {
            fuelSubsystem.moveDividerMotor(dividerVoltage);
            executed = true;
        }
    }

    @Override
    public boolean isFinished(){
        if (executed) {
            System.out.println("LEBRON IS FINISHED");
            return true; 
        }
        return false;
    }
}



