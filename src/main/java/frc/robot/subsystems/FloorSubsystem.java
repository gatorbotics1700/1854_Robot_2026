package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class FloorSubsystem extends SubsystemBase{
    private  TalonFX floorMotor;
    private double floorMotorVoltage;

    public FloorSubsystem(){
        floorMotor = new TalonFX(Constants.FLOOR_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
    }

    //TODO: might need to add current limit and/or motion magic later
   
    public void moveFloor(double voltage){
        floorMotor.setVoltage(voltage);
        floorMotorVoltage = voltage;
    }


    public double getFloorMotorVoltage(Mode currentMode) {
        if(currentMode == Mode.REAL){
            return floorMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return floorMotorVoltage;
        }  
    }

}