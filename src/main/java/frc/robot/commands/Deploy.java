package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;
 

public class Deploy extends Command {
    
    private final Intake IntakeSubsystem;

    public Deploy (Intake intakeSubsystem){
        this.IntakeSubsystem = intakeSubsystem;
        addRequirements(IntakeSubsystem);
    }

    @Override
    public void execute() {
        System.out.println("DEPLOYING");
        IntakeSubsystem.moveDeployMotor(Constants.DEPLOY_MOTOR_POSITION);
    }

    @Override 
    public boolean isFinished () {
        return true;
    }
}
