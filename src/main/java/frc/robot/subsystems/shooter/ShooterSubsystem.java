package frc.robot.subsystems.shooter;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedNetworkNumber;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.controls.MotionMagicVelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class ShooterSubsystem extends SubsystemBase{
    private TalonFX shooterMotor; 
    private TalonFX dividerMotor;
    private double shooterMotorVoltage;
    private double shooterMotorTargetVelocity;
    private double dividerMotorVoltage;
    private long shooterStartTime;
    private TalonFXConfiguration shooterMotorConfigs;
    private Slot0Configs shooterMotorSlot0Configs;
    private MotionMagicVelocityVoltage shooterMotorRequest;
    public static final LoggedNetworkNumber shooterMotorKS = new LoggedNetworkNumber("/Tuning/Shooter/Flywheel kS", 0);
    public static final LoggedNetworkNumber shooterMotorKV = new LoggedNetworkNumber("/Tuning/Shooter/Flywheel kV", 0);
    public static final LoggedNetworkNumber shooterMotorKA = new LoggedNetworkNumber("/Tuning/Shooter/Flywheel kA", 0);
    public static final LoggedNetworkNumber shooterMotorKP = new LoggedNetworkNumber("/Tuning/Shooter/Flywheel kP", 0);
    public static final LoggedNetworkNumber shooterMotorKD = new LoggedNetworkNumber("/Tuning/Shooter/Flywheel kD", 0);
    public static final LoggedNetworkNumber shooterMotorKI = new LoggedNetworkNumber("/Tuning/Shooter/Flywheel kI", 0);
      
    

    // Tune wihtout deploying on advantage scope then copy values over
    
    public ShooterSubsystem(){
        shooterMotor = new TalonFX(Constants.SHOOTER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitShooterMotor();
        dividerMotor = new TalonFX(Constants.DIVIDER_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitDividerMotor();
        shooterMotorConfigs = new TalonFXConfiguration();
        shooterMotorSlot0Configs = shooterMotorConfigs.Slot0;
        shooterMotorRequest = new MotionMagicVelocityVoltage(0);
    }

    @Override
    public void periodic() {
       updateMotorConfigs();
       Logger.recordOutput("mech/shooter/targetVelocity", shooterMotorTargetVelocity);
       Logger.recordOutput("mech/shooter/actualVelocity", shooterMotor.getRotorVelocity().getValueAsDouble());
       shooterMotor.setControl(shooterMotorRequest.withVelocity(shooterMotorTargetVelocity));
    }

    private void updateMotorConfigs(){
        double newKS = shooterMotorKS.get();
        double newKV = shooterMotorKV.get();
        double newKA = shooterMotorKA.get();
        double newKP = shooterMotorKP.get();
        double newKD = shooterMotorKD.get();
        double newKI = shooterMotorKI.get();

        if(newKS != shooterMotorSlot0Configs.kS ||
            newKV != shooterMotorSlot0Configs.kV ||
            newKA != shooterMotorSlot0Configs.kA ||
            newKP != shooterMotorSlot0Configs.kP ||
            newKD != shooterMotorSlot0Configs.kI ||
            newKI != shooterMotorSlot0Configs.kD){
                shooterMotorSlot0Configs.kS = newKS;
                shooterMotorSlot0Configs.kV = newKV;
                shooterMotorSlot0Configs.kA = newKA;
                shooterMotorSlot0Configs.kP = newKP;
                shooterMotorSlot0Configs.kD = newKD;
                shooterMotorSlot0Configs.kI = newKI;

                shooterMotor.getConfigurator().apply(shooterMotorConfigs);
            }

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

    public void setShooterVelocity(double desiredVelocity){
        shooterMotorTargetVelocity = desiredVelocity;
    }

    public void moveShooterMotor(double voltage){
        if (shooterMotorVoltage == 0) {
            shooterStartTime = System.currentTimeMillis();
        }
        shooterMotor.setVoltage(voltage);
        shooterMotorVoltage = voltage;
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
        if(shooterMotorVoltage != 0 && System.currentTimeMillis() > shooterStartTime + 3000){
            return true;
        } else {
            return false;
        }
    }
}