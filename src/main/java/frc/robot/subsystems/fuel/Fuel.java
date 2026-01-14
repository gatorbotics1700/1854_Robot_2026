package frc.robot.subsystems.fuel;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Fuel extends SubsystemBase{
    public  TalonFX intakeShooterMotor;
    public  TalonFX dividerMotor;
    
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