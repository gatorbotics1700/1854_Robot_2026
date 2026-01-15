package frc.robot.subsystems.fuel;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Fuel extends SubsystemBase{
    public  TalonFX ShooterMotor;
    public  TalonFX dividerMotor;
    
    public Fuel(){
        ShooterMotor = new TalonFX(Constants.SHOOTER_MOTOR_CAN_ID, Constants.CANIVORE_BUS_NAME);
        dividerMotor = new TalonFX(Constants.DIVIDER_MOTOR_CAN_ID, Constants.CANIVORE_BUS_NAME);
    }

    @Override
    public void periodic(){
    }

    public void moveShooterMotor(double voltage){
        ShooterMotor.setVoltage(voltage);
    }

    public void moveDividerMotor(double voltage) {
        dividerMotor.setVoltage(voltage);
    }

}