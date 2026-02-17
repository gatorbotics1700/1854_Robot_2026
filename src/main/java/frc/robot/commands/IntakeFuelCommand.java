package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.IntakeSubsystem;


public class IntakeFuelCommand extends Command {
    
    private final double voltage;

    private final IntakeSubsystem intakeSubsystem;

     public IntakeFuelCommand(IntakeSubsystem intakeSubsystem, double voltage) {
        this.intakeSubsystem = intakeSubsystem;
        this.voltage = voltage;
        System.out.println("lebron intake command initialized");
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.moveIntakeMotor(voltage);
    
    }
    
    @Override
    public boolean isFinished() {
        if(intakeSubsystem.getIntakeMotorVoltage() == voltage){ //TODO:find out how to deal with this bcs it needs a mode
            return true;
        } 
        return false;
        // TODO: should we check what type of condition needs to be met
    }
}
