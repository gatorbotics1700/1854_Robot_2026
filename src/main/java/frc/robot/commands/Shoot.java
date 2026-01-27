package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.fuel.Fuel;

public class Shoot extends Command{

    private Fuel fuelSubsystem;
    private double shooterVoltage;
    private double dividerVoltage;
    
    public Shoot(Fuel fuelSubsystem, double shooterVoltage, double dividerVoltage){
        this.fuelSubsystem = fuelSubsystem; 
        this.shooterVoltage = shooterVoltage;
        this.dividerVoltage = dividerVoltage;
        System.out.println("shoot has been shot");
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        System.out.println("SHOOTING");
        fuelSubsystem.moveDividerMotor(dividerVoltage);
        fuelSubsystem.moveShooterMotor(shooterVoltage);
    }

    @Override
    public boolean isFinished(){
        if (shooterVoltage == fuelSubsystem.getShooterMotorVoltage(Constants.currentMode)) {
          return true; 
        }
        System.out.println("LEBRON IS FINISHED");
        return false;
        // TODO: should we check what type of condition needs to be met
    }
}



