package frc.robot.commands;

import frc.robot.Constants;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.fuel.Fuel;

public class TopRelease extends Command {
    
    private Fuel fuelSubsystem;
    
    public TopRelease(Fuel fuelSubsystem){
        this.fuelSubsystem = fuelSubsystem; 
        addRequirements(fuelSubsystem);
    }

    @Override
    public void execute(){
        fuelSubsystem.moveDividerMotor(Constants.RELEASE_DIVIDER_MOTOR_VOLTAGE);
        fuelSubsystem.moveShooterMotor(Constants.RELEASE_SHOOTER_MOTOR_VOLTAGE);
    }

    @Override
    public boolean isFinished(){
        return true;
    }
}
