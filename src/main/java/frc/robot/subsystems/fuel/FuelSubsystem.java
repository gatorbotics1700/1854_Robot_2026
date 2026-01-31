package frc.robot.subsystems.fuel;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class FuelSubsystem extends SubsystemBase{
    private TalonFX shooterMotor; 
    private  TalonFX dividerMotor;
    private double shooterMotorVoltage;
    
    public FuelSubsystem(){
            shooterMotor = new TalonFX(Constants.SHOOTER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
            dividerMotor = new TalonFX(Constants.DIVIDER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
    }


    @Override
    public void periodic(){
        // TODO: take this out
    }

    public void moveShooterMotor(double voltage){
        shooterMotor.setVoltage(voltage);
        shooterMotorVoltage = voltage;
    }

    public void moveDividerMotor(double voltage) {
        dividerMotor.setVoltage(voltage);
    }

    public double getShooterMotorVoltage(Mode currentMode) {
        if (currentMode == Mode.REAL) {
            return shooterMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return shooterMotorVoltage;
        }
    }
}