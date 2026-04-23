package frc.robot.subsystems.intake;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class IntakeSubsystem extends SubsystemBase{
    private  TalonFX intakeMotor;
    private  TalonFX deployMotor;
    private double intakeMotorVoltage;
    private boolean isDeployed;
    private double deployMotorVoltage;

    public IntakeSubsystem(){
        intakeMotor = new TalonFX(Constants.INTAKE_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitIntakeMotor();
        deployMotor = new TalonFX(Constants.DEPLOY_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        // Set to brake mode to prevent intake from flopping out when moving
        currentLimitDeployMotor();
        deployMotor.setNeutralMode(NeutralModeValue.Brake);
        
        isDeployed = false;
        System.out.println("lebron initializing");
    }

    private void currentLimitDeployMotor(){
        // Limits need to be low since, on deploy, motor will continuously push on the bumpers
        CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
        limits.SupplyCurrentLimitEnable = true;
        limits.SupplyCurrentLimit = Constants.DEPLOY_CURRENT_LIMIT;
        limits.StatorCurrentLimitEnable = true;
        limits.StatorCurrentLimit = Constants.DEPLOY_CURRENT_LIMIT;
        deployMotor.getConfigurator().apply(limits);
    }

    private void currentLimitIntakeMotor(){
        CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
        limits.SupplyCurrentLimitEnable = true;
        limits.SupplyCurrentLimit = Constants.INTAKE_CURRENT_LIMIT; //assuming that only drivetrain will also be running

        intakeMotor.getConfigurator().apply(limits);
    }
   
    public void moveIntakeMotor(double voltage){
        intakeMotor.setVoltage(voltage);
        intakeMotorVoltage = voltage;
    }

    public void setIsDeployed(boolean deployStat){
        isDeployed = deployStat;
    }

    public void setDeployMotorVoltage(double voltage) {
        //deployMotor.setControl(
          //  m_request.withPosition(degreesToRevs(IntakeConstants.RETRACTED_ANGLE_DEGREES)));
        deployMotor.setVoltage(voltage);
        deployMotorVoltage = voltage;
    }

    public double getDeployMotorVoltage(Mode currentMode) {
        if(currentMode == Mode.REAL) {
            return deployMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return deployMotorVoltage;
        }
        
    }  

    public double getIntakeMotorVoltage(Mode currentMode) {
        if(currentMode == Mode.REAL){
            return intakeMotor.getMotorVoltage().getValueAsDouble();
        } else {
            return intakeMotorVoltage;
        }  
    }

    public double getDeployMotorCurrent(Mode currentMode) {
        if(currentMode == Mode.REAL){
            return deployMotor.getStatorCurrent().getValueAsDouble();
        } else{
            return 0;
        }
        
    }  

    public boolean isDeployed() {
        return isDeployed;
    }
    

}