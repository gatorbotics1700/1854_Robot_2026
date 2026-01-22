package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.fuel.Fuel;
// TODO can we combine this with top release

public class Shoot extends Command{

    private Fuel fuelSubsystem;
    
    public Shoot(Fuel fuelSubsystem){
        this.fuelSubsystem = fuelSubsystem; 
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        System.out.println("SHOOTING");
        fuelSubsystem.moveDividerMotor(Constants.DIVIDER_MOTOR_VOLTAGE);
        fuelSubsystem.moveShooterMotor(Constants.SHOOTER_MOTOR_VOLTAGE);
    }

    @Override
    public boolean isFinished(){
        return true;
        // TODO: should we check what type of condition needs to be met
    }
}

