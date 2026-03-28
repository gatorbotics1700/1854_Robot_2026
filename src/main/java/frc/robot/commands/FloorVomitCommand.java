package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.subsystems.shooter.ShooterSubsystem;
import frc.robot.Constants.Mode;

public class FloorVomitCommand extends Command{
    private final IntakeSubsystem intakeSubsystem;
    private double intakeVoltage;
    private double floorVoltage;
    private boolean executed;
    private final ShooterSubsystem shooterSubsystem;

    public FloorVomitCommand(IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem, double intakeVoltage, double floorVoltage, Mode mode){
        this.intakeSubsystem = intakeSubsystem;
        this.intakeVoltage = intakeVoltage;
        this.floorVoltage = floorVoltage;
        this.shooterSubsystem = shooterSubsystem;
        executed = false;
        addRequirements(intakeSubsystem);
        addRequirements(shooterSubsystem);
    }
    @Override
    public void execute() {
        intakeSubsystem.moveIntakeMotor(intakeVoltage);
        shooterSubsystem.moveFloorMotor(floorVoltage);
        executed = true;  
    }
    
    @Override
    public boolean isFinished() {
        return executed;
    }

}