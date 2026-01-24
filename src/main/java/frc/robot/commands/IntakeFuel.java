package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.intake.Intake;


public class IntakeFuel extends Command {
    
    private final double voltage;

    private final Intake intakeSubsystem;

     public IntakeFuel(Intake intakeSubsystem, double voltage) {
        this.intakeSubsystem = intakeSubsystem;
        this.voltage = voltage;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void execute() {
        intakeSubsystem.moveFuelMotor(voltage);
    
    }
    
    @Override
    public boolean isFinished() {
        if(intakeSubsystem.getFuelMotorVoltage() == voltage){
            return true;
        } 
        return false;
        // TODO: should we check what type of condition needs to be met
    }
}
