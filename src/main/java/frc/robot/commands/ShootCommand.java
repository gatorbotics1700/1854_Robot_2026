package frc.robot.commands;

import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class ShootCommand extends Command{

    private ShooterSubsystem shooterSubsystem;
    private double dividerVoltage;
    private double floorVoltage;
    private boolean executed;
    private double shooterVelocity;
    
    public ShootCommand(ShooterSubsystem shooterSubsystem, double shooterVelocity, double dividerVoltage, double floorVoltage){
        this.shooterSubsystem = shooterSubsystem; 
        this.dividerVoltage = dividerVoltage;
        this.shooterVelocity = shooterVelocity;
        this.floorVoltage = floorVoltage;
        executed = false;
        addRequirements(shooterSubsystem);
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
        shooterSubsystem.setShooterVelocity(shooterVelocity);
        shooterSubsystem.moveDividerMotor(dividerVoltage);
        shooterSubsystem.moveFloorMotor(floorVoltage);
        executed = true;


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



