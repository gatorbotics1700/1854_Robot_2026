import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;



public class Intake extends CommandBase{

    private final Fuel fuelSubsystem;
    
    
    public Intake(Fuel fuelSubsystem){
        this.fuelSubsystem = fuelSubsystem;
        addRequirements(fuelSubsystem);
    }

   @Override
   execute(){
        fuelSubsystem.moveIntakeShooterMotor(INTAKE_MOTOR_VOLTAGE);
    // set motor make an instance of. a motor and then set motor volatge to intake a certain volatge make a constant in the constants doc of that volatge
    // to set mother volt us set motor volateg (volatge)

   }

   @Override
   isFinished(){
    return true;
   }



}