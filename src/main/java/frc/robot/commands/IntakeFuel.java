package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;


public class IntakeFuel extends Command{

    private final Intake intakeSubsystem;
    
    
    public IntakeFuel(Intake intakeSubsystem){
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

   @Override
   public void execute(){
        intakeSubsystem.moveFuelMotor(Constants.FUEL_MOTOR_VOLTAGE);
    // set motor make an instance of. a motor and then set motor volatge to intake a certain volatge make a constant in the constants doc of that volatge
    // to set mother volt us set motor volateg (volatge)

   }

   @Override
   public boolean isFinished(){
    return true;
   }



}