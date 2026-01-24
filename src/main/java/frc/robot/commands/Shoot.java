package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.fuel.Fuel;

public class Shoot extends Command{

    private Fuel fuelSubsystem;
    private double shooterVoltage;
    
    public Shoot(Fuel fuelSubsystem, double shooterVoltage){
        this.fuelSubsystem = fuelSubsystem; 
        this.shooterVoltage = shooterVoltage;
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        System.out.println("SHOOTING");
        fuelSubsystem.moveDividerMotor(Constants.DIVIDER_MOTOR_VOLTAGE);
        fuelSubsystem.moveShooterMotor(shooterVoltage);
    }

    @Override
    public boolean isFinished(){
        if (shooterVoltage == fuelSubsystem.getShooterMotorVoltage(Constants.currentMode)) {
           return true; 
        }
        return false;
        // TODO: should we check what type of condition needs to be met
    }
}



