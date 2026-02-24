package frc.robot.subsystems.led;


import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.configs.LEDConfigs;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StripTypeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;



public class LedSubsystem extends SubsystemBase{

    private final CANdle ctrLED;

    StripTypeValue stripType = StripTypeValue.RGB;

    LEDConfigs config_one = new LEDConfigs().withBrightnessScalar(0)
        .withStripType(stripType);

    CANdleConfiguration config = new CANdleConfiguration().withLED(config_one);

    public LedSubsystem() {

        ctrLED = new CANdle(Constants.CTRLED_CAN_ID);
        setSolidColor(255,255,50); /* yellow */

    }

    public void setSolidColor(int r, int g, int b) {
        
        CANdleConfiguration config = new CANdleConfiguration();
        
        RGBWColor newColor = new RGBWColor(r, g, b);
        SolidColor solidColor = new SolidColor(0,7).withColor(newColor);

        ctrLED.getConfigurator().apply(config);
        ctrLED.setControl(solidColor);

    }

}
