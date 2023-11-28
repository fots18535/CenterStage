package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

public class RedStopper implements Stopper {
    NormalizedColorSensor colorSensor;
    long startTime = 0;
    final float[] hsvValues = new float[3];
    final float hue = 214;
    final float hmin = hue - (hue * 0.1f);
    final float hmax = hue + (hue * 0.1f);

    final float sat = 0.818f;
    final float smin = sat - (sat * 0.1f);
    final float smax = sat + (sat * 0.1f);

    public RedStopper(NormalizedColorSensor cs) {
        colorSensor = cs;
    }

    @Override
    public void begin() {
        startTime = System.currentTimeMillis();
    }

    @Override
    public boolean stop() {
        NormalizedRGBA rgba = colorSensor.getNormalizedColors();
        // Update the hsvValues array by passing it to Color.colorToHSV()
        Color.colorToHSV(rgba.toColor(), hsvValues);

        float chue = hsvValues[0];
        float csat = hsvValues[1];

        return (hmin < chue && hmax > chue && smin < csat && smax > csat) || System.currentTimeMillis() - startTime > 5000;
    }
}
