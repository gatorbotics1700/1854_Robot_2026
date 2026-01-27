package frc.robot.subsystems.intake;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class Intake extends SubsystemBase{
    private  TalonFX fuelMotor;
    private  TalonFX deployMotor;
    private double intakeMotorVoltage;
    private boolean isDeployed;
    
    public Intake(){
        fuelMotor= new TalonFX(Constants.FUEL_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        deployMotor = new TalonFX(Constants.DEPLOY_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        isDeployed = false;
    }

    @Override
    public void periodic(){
    }
   
    public void moveFuelMotor(double voltage){
        fuelMotor.setVoltage(voltage);
        intakeMotorVoltage = voltage;
    }

    public double getFuelMotorVoltage(){
        return fuelMotor.getMotorVoltage().getValueAsDouble();
    }

    public void setIsDeployed(boolean deployStat){
        isDeployed = deployStat;
    }


    public void moveDeployMotor(double position) {
       // deployMotor.set(new PositionDutyCycle(position)); // TODO fix
       // can use a PID
    }

    public double getDeployMotorPosition() {
        return deployMotor.getPosition().getValueAsDouble();
    }

    public double getIntakeMotorVoltage(Mode currentMode) {
        if(currentMode == Mode.REAL){
            return fuelMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return intakeMotorVoltage;
        }  
    }

    public boolean isDeployed() {
        return isDeployed;
    }
}