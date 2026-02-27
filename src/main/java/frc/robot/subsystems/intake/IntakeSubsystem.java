package frc.robot.subsystems.intake;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.Mode;

public class IntakeSubsystem extends SubsystemBase{
    private  TalonFX intakeMotor;
    private  TalonFX deployMotor;
    private double intakeMotorVoltage;
    private boolean isDeployed;
    private double deployMotorVoltage;
    // private final TalonFXConfiguration deployTalonFXConfigs;
    // private static MotionMagicExpoVoltage m_request;
    //TODO: implement motion magic
    public IntakeSubsystem(){
        intakeMotor= new TalonFX(Constants.INTAKE_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitIntakeMotor();
        //TODO: implement motion magic
        deployMotor = new TalonFX(Constants.DEPLOY_MOTOR_CAN_ID, Constants.MECH_CANBUS_NAME);
        currentLimitDeployMotor();
        isDeployed = false;
        System.out.println("lebron initializing");
        //deployTalonFXConfigs = new TalonFXConfiguration();
        // TODO: TUNE ALL OF THESE
        //Slot0Configs slot0Configs = deployTalonFXConfigs.Slot0;

        /*slot0Configs.kG = 0.2128; // Add _ V output to overcome gravity
        slot0Configs.kS = 0.25; // Add _ V output to overcome static friction
        slot0Configs.kV =
            0.16; // A velocity target of 1 rps results in _ V output (should be somewhere between 0.12
        // and 0.2)
        slot0Configs.kA = 0.01; // An acceleration of 1 rps/s requires 0.01 V output

        slot0Configs.kP = 4.8; // A position error of 2.5 rotations results in 12V output
        slot0Configs.kI = 0; // no output for integrated error
        slot0Configs.kD = 0.1; // a velocity error of 1 rps results in 0.1 V output

        // MOTION MAGIC EXPO
        MotionMagicConfigs motionMagicConfigs = deployTalonFXConfigs.MotionMagic;

        motionMagicConfigs.MotionMagicCruiseVelocity = 0; // 0 gives us unlimited cruise velocity
        motionMagicConfigs.MotionMagicExpo_kV = 0.16; // kV is around 0.12 V/rps, might be 0.12-0.2
        motionMagicConfigs.MotionMagicExpo_kA =
            0.1; // Use a slower kA of 0.1 V/(rps/s) - the larger the kA, the smoother and slower

        deployMotor.getConfigurator().apply(deployTalonFXConfigs);

        m_request = new MotionMagicExpoVoltage(0);
        */
    }

    private void currentLimitDeployMotor(){
        CurrentLimitsConfigs limits = new CurrentLimitsConfigs();
        limits.SupplyCurrentLimitEnable = true;
        limits.SupplyCurrentLimit = Constants.DEPLOY_CURRENT_LIMIT; // assuming that only drivetrain will also be running

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