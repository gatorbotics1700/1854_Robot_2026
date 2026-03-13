package frc.robot.commands;

import com.ctre.phoenix6.controls.VelocityVoltage;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class ShootCommand extends Command{

    private ShooterSubsystem shooterSubsystem;
    private double dividerVoltage;
    private boolean executed;
    private double shooterVelocity;
    
    public ShootCommand(ShooterSubsystem shooterSubsystem, double shooterVelocity, double dividerVoltage){
        this.shooterSubsystem = shooterSubsystem; 
        this.dividerVoltage = dividerVoltage;
        this.shooterVelocity = shooterVelocity;
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
        if (dividerVoltage > 0 && shooterSubsystem.shooterWarmedUp()) {
            shooterSubsystem.moveDividerMotor(dividerVoltage);
            executed = true;
        } else if (dividerVoltage == 0) {
            shooterSubsystem.moveDividerMotor(dividerVoltage);
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



