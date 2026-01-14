import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants;


public class Shoot extends Command{

    private final Fuel fuelSubsystem;
    private final double voltage; 
    
    public Shoot (Fuel fuelSubsystem){
        this.fuelSubsystem = fuelSubsystem; 
        addRequirements(fuelSubsystem);
         this.voltage = voltage;
    }
    @Override
    execute(){
        if (voltage < 0){
            System.out.println("SHOOTING");
            if(voltage == Constants.SHOOTER_MOTOR_VOLTAGE){
                IntakeShooterSubsystem.moveIntakeShooterMotor(Constants.SHOOTER_MOTOR_VOLTAGE)
                }
            }
        }

    }

    @Override
    isFinished(){
        return true;
    }

}
