package frc.robot.subsystems.intake;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.ControlModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Intake extends SubsystemBase{
    public  TalonFX fuelMotor;
    public  TalonFX deployMotor;
    
    public Intake(){
        fuelMotor= new TalonFX(Constants.FUEL_MOTOR_CAN_ID, Constants.CANIVORE_BUS_NAME);
        deployMotor = new TalonFX(Constants.DEPLOY_MOTOR_CAN_ID, Constants.CANIVORE_BUS_NAME);
    }

    @Override
    public void periodic(){
    }
   
    public void moveFuelMotor(double voltage){
        fuelMotor.setVoltage(voltage);
    }

    public void moveDeployMotor(double position) {
       // deployMotor.set(new PositionDutyCycle(position)); // TODO fix
       // can use a PID
    }

    

}