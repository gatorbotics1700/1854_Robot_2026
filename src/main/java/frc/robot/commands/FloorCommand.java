package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.FloorSubsystem;

public class FloorCommand extends Command{

    private FloorSubsystem floorSubsystem;
    private boolean executed;
    private double floorVoltage;
    
    public FloorCommand(FloorSubsystem floorSubsystem, double floorVoltage){
        this.floorSubsystem = floorSubsystem; 
        this.floorVoltage = floorVoltage;
        executed = false;
        addRequirements(floorSubsystem);
    }

    @Override
    public void execute(){
        floorSubsystem.moveFloor(floorVoltage);
        executed = true;
    }

    @Override
    public boolean isFinished(){
        if (executed) {
            System.out.println("LEBRON IS FINISHED");
            return true; 
        }
        return false;
    }
}



