package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class Divider extends CommandBase {
        private final Fuel fuelSubsystem;
        public Intake(fuelSubsystem){
            this.fuelSubsystem = fuelSubsystem;
            addRequirements(finalSubsystem);
        }

    
    execute(){
        
    }


    ifFinished(){

    }
}
