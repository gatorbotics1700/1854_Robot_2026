package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;
 

public class Retract extends Command {
    
    private final Retract IntakeSubsystem;
    public Retract (Intake intakeSubsystem){
        this.IntakeSubsystem = intakeSubsystem();
        addRequirements(IntakeSubsystem);
    }

    @Override
    execute () {
        System.out.println("RETRACTING")
        IntakeSubsystem.moveRetractMotor(Constants.RETRACT_MOTOR_POSITION);
    }

    @Override 
    public boolean isFinished () {
        return true;
    }
}