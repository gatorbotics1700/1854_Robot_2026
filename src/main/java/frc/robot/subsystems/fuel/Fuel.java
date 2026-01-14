package frc.robot.subsystems;

import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Fuel extends SubsystemBase{
    public final TalonFX intakeShooterMotor;
    public final TalonFX dividerMotor;
    
    public Fuel(){
        intakeShooterMotor = new TalonFX(Constants.INTAKE_SHOOTER_MOTOR_CAN_ID, Constants.CANIVORE_BUS_NAME);
        dividerMotor = new TalonFX(Constants.DIVIDER_MOTOR_CAN_ID, Constants.CANIVORE_BUS_NAME);
    }

    @Override
    public void periodic(){
    }

    public void moveIntakeShooterMotor(double voltage){
        intakeShooterMotor.setVoltage(voltage);
    }

    public void moveDividerMotor(double voltage) {
        dividerMotor.setVoltage(voltage);
    }
}