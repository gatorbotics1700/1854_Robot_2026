package frc.robot.commands;

import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class ShootCommand extends Command{

    private ShooterSubsystem fuelSubsystem;
    private double dividerVoltage;
    private boolean executed;
    private double shooterVelocity;
    
    public ShootCommand(ShooterSubsystem fuelSubsystem, double shooterVelocity, double dividerVoltage){
        this.fuelSubsystem = fuelSubsystem; 
        this.dividerVoltage = dividerVoltage;
        this.shooterVelocity = shooterVelocity;
        executed = false;
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        executed = false;
        // Case 1: we want to stop the top wheels
        if (shooterVelocity == 0) {
            System.out.println("STOP SHOOTING");
        }
        // Case 2: we want to move the top wheels
        else {
            System.out.println("SHOOTING");
        }
        fuelSubsystem.setShooterVelocity(shooterVelocity);
        if (dividerVoltage > 0 && fuelSubsystem.shooterWarmedUp()) {
            fuelSubsystem.moveDividerMotor(dividerVoltage);
            executed = true;
        } else if (dividerVoltage == 0) {
            fuelSubsystem.moveDividerMotor(dividerVoltage);
            executed = true;

        }

    }

    @Override
    public boolean isFinished(){
        if (executed) {
            System.out.println("SHOOTING IS FINISHED");
            return true; 
        }
        return false;
    }
}



