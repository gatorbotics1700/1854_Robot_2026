package frc.robot.subsystems.led;


import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.configs.LEDConfigs;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.hardware.CANdle;
import com.ctre.phoenix6.signals.RGBWColor;
import com.ctre.phoenix6.signals.StripTypeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;



public class LedSubsystem extends SubsystemBase{

    private final CANdle ctrLED;
    private int ledR;
    private int ledG;
    private int ledB;
    private boolean isBlinking;

    StripTypeValue stripType = StripTypeValue.RGB;

    LEDConfigs config_one = new LEDConfigs().withBrightnessScalar(0)
        .withStripType(stripType);

    CANdleConfiguration config = new CANdleConfiguration().withLED(config_one);

    public LedSubsystem() {
        ctrLED = new CANdle(Constants.CTRLED_CAN_ID);
        setColor(255,255,50); /* yellow */
        setSolid();
        isBlinking = false;
    }

    public RGBWColor getColor(){
        return new RGBWColor(ledR,ledG,ledB);
    }
        
    public void setBlinking(){
        RGBWColor newColor = new RGBWColor(255,0,255);
        SolidColor solidColor = new SolidColor(0,3).withColor(newColor);
        ctrLED.setControl(solidColor);
        isBlinking = true;
        //System.out.println("Lebron blinking");
    }

    public void setColor(int r, int g, int b){
        ledR = r;
        ledG = g;
        ledB = b;
    }
    public void setSolid() {
        RGBWColor newColor = new RGBWColor(ledR, ledG, ledB);
        SolidColor solidColor = new SolidColor(0,7).withColor(newColor);
        ctrLED.setControl(solidColor);
        isBlinking = false;
    }

}


