package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;

public class StopIntake extends Command {

    private final Intake intakeSubsystem;

    public StopIntake(Intake intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.moveFuelMotor(0.0);
        intakeSubsystem.moveDeployMotor(0.0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }

}