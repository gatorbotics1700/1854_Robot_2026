package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;
 
// TODO see if we can merge this with deploy
public class Retract extends Command {
    
    private final Intake intakeSubsystem;

    public Retract (Intake intakeSubsystem){
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        System.out.println("RETRACTING");
        intakeSubsystem.moveDeployMotor(Constants.RETRACT_MOTOR_POSITION);
    }

    @Override 
    public boolean isFinished () {
        return true;
        // TODO: should we check position 
    }
}