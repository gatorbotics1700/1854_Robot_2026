import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;


public class Shoot extends Command{

    private final Fuel fuelSubsystem;
    
    public Shoot (){
        Fuel fuelSubsystem = new Fuel(); 
        addRequirements(fuelSubsystem);
    }

    @Override
    execute(){
        System.out.println("SHOOTING");
        fuelSubsystem.moveDividerMotor(Constants.DIVIDER_TO_OUTAKE_VOLTAGE);
        fuelSubsystem.moveIntakeShooterMotor(Constants.SHOOTER_MOTOR_VOLTAGE);
    }

    @Override
    isFinished(){
        return true;
    }
}

