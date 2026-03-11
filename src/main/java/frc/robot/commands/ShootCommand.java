package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.shooter.ShooterSubsystem;

public class ShootCommand extends Command{

    private ShooterSubsystem fuelSubsystem;
    private double shooterVoltage;
    private double dividerVoltage;
    private boolean executed;
    private double shooterVelocity;
    
    public ShootCommand(ShooterSubsystem fuelSubsystem, double shooterVoltage, double dividerVoltage){
        this.fuelSubsystem = fuelSubsystem; 
        this.shooterVoltage = shooterVoltage;
        this.dividerVoltage = dividerVoltage;
        shooterVelocity = 0;
        executed = false;
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        executed = false;
        /*if (shooterVoltage != 0) {
            System.out.println("SHOOTING");
        }
        fuelSubsystem.moveShooterMotor(shooterVoltage);
        if (shooterVoltage == 0 || fuelSubsystem.shooterWarmedUp()) {
            fuelSubsystem.moveDividerMotor(dividerVoltage);
            executed = true;
        }*/
        if (shooterVelocity != 0) {
            System.out.println("SHOOTING");
        }
        fuelSubsystem.setShooterVelocity(40);
        if (shooterVelocity == 0 || fuelSubsystem.shooterWarmedUp()) {
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



