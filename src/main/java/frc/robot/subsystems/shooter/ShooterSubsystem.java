package frc.robot.subsystems.shooter;
import org.littletonrobotics.junction.Logger;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class ShooterSubsystem extends SubsystemBase{
    private TalonFX shooterMotor; 
    private TalonFX dividerMotor;
    private double shooterMotorVoltage;
    private double shooterMotorTargetVelocity = 0;
    private double dividerMotorVoltage;
    private long shooterStartTime;
    private TalonFXConfiguration shooterMotorConfigs;
    private Slot0Configs shooterMotorSlot0Configs;
    
    public ShooterSubsystem(){
        shooterMotor = new TalonFX(Constants.SHOOTER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitShooterMotor();
        dividerMotor = new TalonFX(Constants.DIVIDER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitDividerMotor();
        shooterMotorConfigs = new TalonFXConfiguration();
        shooterMotorSlot0Configs = shooterMotorConfigs.Slot0;
        shooterMotorSlot0Configs.kS = 0;
        shooterMotorSlot0Configs.kV = .125;
        shooterMotorSlot0Configs.kP = 0;
        shooterMotor
            .getConfigurator()
            .apply(shooterMotorConfigs.withSlot0(shooterMotorSlot0Configs));
    }

    @Override
    public void periodic() {
       shooterMotor.setControl(new VelocityVoltage(shooterMotorTargetVelocity));
       Logger.recordOutput("mech/shooter/targetVelocity", shooterMotorTargetVelocity);
       Logger.recordOutput("mech/shooter/actualVelocity", shooterMotor.getRotorVelocity().getValueAsDouble());
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
        limits.SupplyCurrentLimit = 30; // assuming that only drivetrain and shooterMotor will also be running

        dividerMotor.getConfigurator().apply(limits);
    }

    public void setShooterVelocity(double desiredVelocity){
        if (shooterMotorTargetVelocity == 0) {
            shooterStartTime = System.currentTimeMillis();
        }
        shooterMotorTargetVelocity = desiredVelocity;
    }

    public void moveDividerMotor(double voltage) {
        dividerMotor.setVoltage(voltage);
        dividerMotorVoltage = voltage;
    }

    public double getShooterMotorVoltage(Mode currentMode) {
        if (currentMode == Mode.REAL) {
            return shooterMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return shooterMotorVoltage;
        }
    }

    public double getDividerMotorVoltage(Mode currentMode) {
        if (currentMode == Mode.REAL) {
            return dividerMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return dividerMotorVoltage;
        }
    }

    public boolean shooterOn(){
        if(dividerMotorVoltage != 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean shooterWarmedUp(){
        if(shooterMotorTargetVelocity != 0 && System.currentTimeMillis() > shooterStartTime + 3000){
            return true;
        } else {
            return false;
        }
    }
}