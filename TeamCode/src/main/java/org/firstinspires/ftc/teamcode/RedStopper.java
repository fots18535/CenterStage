package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;

public class RedStopper implements Stopper {
    ColorSensor colorSensor;
    long startTime = 0;

    public RedStopper(ColorSensor cs) {
        colorSensor = cs;
    }

    @Override
    public void begin() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean stop() {
        return colorSensor.red() > 1000 || System.currentTimeMillis() - startTime > 5000;
    }
}
