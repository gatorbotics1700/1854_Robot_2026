package frc.robot.subsystems.intake;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class IntakeSubsystem extends SubsystemBase{
    private  TalonFX fuelMotor;
    private  TalonFX deployMotor;
    private double intakeMotorVoltage;
    private boolean isDeployed;
    private double deployMotorPosition;
    
    public IntakeSubsystem(){
        fuelMotor= new TalonFX(Constants.INTAKE_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        deployMotor = new TalonFX(Constants.DEPLOY_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitDeployMotor();
        isDeployed = false;
        System.out.println("lebron initializing");
    }

    private void currentLimitDeployMotor(){
        CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
        limits.SupplyCurrentLimitEnable = true;
        limits.SupplyCurrentLimit = 60;

        deployMotor.getConfigurator().apply(limits);
    }
   
    public void moveFuelMotor(double voltage){
        fuelMotor.setVoltage(Math.max(voltage, 2));
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
       deployMotorPosition = position;
    }

    public double getDeployMotorPosition(Mode currentMode) {
        if(currentMode == Mode.REAL) {
            return deployMotor.getPosition().getValueAsDouble();
        } else {
            return deployMotorPosition;
        }
        
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