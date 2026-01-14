package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.fuel.Fuel;

public class GroundRelease extends Command{

    private final Fuel fuelSubsystem;
    
    public GroundRelease(Fuel fuelSubsystem){
        this.fuelSubsystem = fuelSubsystem;
        addRequirements(fuelSubsystem);
    }

   @Override
   public void execute(){
        fuelSubsystem.moveDividerMotor(Constants.DIVIDER_TO_OUTTAKE_VOLTAGE);
        fuelSubsystem.moveIntakeShooterMotor(Constants.OUTTAKE_MOTOR_VOLTAGE);
    // set motor make an instance of a motor and then set motor volatge to intake a certain volatge make a constant in the constants doc of that volatge
    // to set mother volt us set motor volateg (volatge)
   }

   @Override
   public boolean isFinished(){
       return true;
      }



}