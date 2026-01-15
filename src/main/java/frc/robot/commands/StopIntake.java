package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;


public class StopIntake extends Command{

    private final Intake intakeSubsystem;
    
    public StopIntake(Intake fuelSubsystem){
        this.intakeSubsystem = fuelSubsystem;
        addRequirements(intakeSubsystem);
    }

   @Override
   public void execute(){
        intakeSubsystem.moveFuelMotor(0.0);
        intakeSubsystem.moveDeployMotor(0.0);
    // set motor make an instance of. a motor and then set motor volatge to intake a certain volatge make a constant in the constants doc of that volatge
    // to set mother volt us set motor volateg (volatge)

   }

   @Override
   public boolean isFinished(){
    return true;
   }



}