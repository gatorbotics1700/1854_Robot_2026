package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;
 

public class Deploy extends Command {
    
    private final Intake intakeSubsystem;

    public Deploy (Intake intakeSubsystem){
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        System.out.println("DEPLOYING");
        intakeSubsystem.moveDeployMotor(Constants.DEPLOY_MOTOR_POSITION);
    }

    @Override 
    public boolean isFinished () {
        return true;
        // TODO: should we check if the position is close enough
    }
}
