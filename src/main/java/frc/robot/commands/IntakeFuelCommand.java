package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;
import frc.robot.Constants.Mode;


public class IntakeFuelCommand extends Command {
    
    private final double voltage;
    private final Mode mode;
    private final IntakeSubsystem intakeSubsystem;

     public IntakeFuelCommand(IntakeSubsystem intakeSubsystem, double voltage, Mode mode) {
        this.intakeSubsystem = intakeSubsystem;
        this.voltage = voltage;
        this.mode = mode;
        System.out.println("lebron intake command initialized");
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.moveIntakeMotor(voltage);
    
    }
    
    @Override
    public boolean isFinished() {
        if(intakeSubsystem.getIntakeMotorVoltage(mode) == voltage){
            return true;
        } 
        return false;
        // TODO: should we check what type of condition needs to be met
    }
}
