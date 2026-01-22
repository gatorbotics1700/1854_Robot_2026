package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;
// TODO can we merge this with intake fuel and stop intake
public class GroundRelease extends Command{

    private final Intake intakeSubsystem;
    
    public GroundRelease(Intake intakeSubsystem){
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

   @Override
   public void execute(){
        intakeSubsystem.moveFuelMotor(Constants.OUTAKE_MOTOR_VOLTAGE);
    
   }

   @Override
   public boolean isFinished(){
       return true;
       // TODO: should we check what type of condition needs to be met
    }
      



}