package frc.robot.subsystems.fuel;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
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
        currentLimitShooterMotor();
        dividerMotor = new TalonFX(Constants.DIVIDER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitDividerMotor();
    }

    private void currentLimitShooterMotor(){
        CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
        limits.SupplyCurrentLimitEnable = true;
        limits.SupplyCurrentLimit = 45; // assuming that only drivetrain and dividerMotor will also be running

        shooterMotor.getConfigurator().apply(limits);
    }

    private void currentLimitDividerMotor(){
        CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
        limits.SupplyCurrentLimitEnable = true;
        limits.SupplyCurrentLimit = 15; // assuming that only drivetrain and shooterMotor will also be running

        dividerMotor.getConfigurator().apply(limits);
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

    public boolean shooterOn(){
        if(shooterMotorVoltage != 0){
            return true;
        } else{
            return false;
        }
    }
}