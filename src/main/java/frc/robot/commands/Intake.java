package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.subsystems.fuel.Fuel;



public class Intake extends Command{

    private final Fuel fuelSubsystem;
    
    
    public Intake(Fuel fuelSubsystem){
        this.fuelSubsystem = fuelSubsystem;
        addRequirements(fuelSubsystem);
    }

   @Override
   public void execute(){
        fuelSubsystem.moveIntakeShooterMotor(Constants.INTAKE_SHOOTER_MOTOR_VOLTAGE);
    // set motor make an instance of. a motor and then set motor volatge to intake a certain volatge make a constant in the constants doc of that volatge
    // to set mother volt us set motor volateg (volatge)

   }

   @Override
   public boolean isFinished(){
    return true;
   }



}