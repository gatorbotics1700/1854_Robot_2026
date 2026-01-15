package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;



public class Shooter extends CommandBase {
        private final Fuel fuelSubsystem;
        public Shooter(fuelSubsystem){
            this.fuelSubsystem = fuelSubsystem;
            addRequirements(finalSubsystem);
        }

    
    execute(){
        
    }


    ifFinished(){

    }
}

