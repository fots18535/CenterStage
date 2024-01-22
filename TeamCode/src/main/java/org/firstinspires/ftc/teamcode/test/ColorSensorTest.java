package org.firstinspires.ftc.teamcode.test;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@TeleOp(group="Tests")
public class ColorSensorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        NormalizedColorSensor color = hardwareMap.get(NormalizedColorSensor.class, "color1");;
        if (color instanceof SwitchableLight) {
            ((SwitchableLight)color).enableLight(true);
        }
        color.setGain(2.0f);

        // See http://web.archive.org/web/20190311170843/https://infohost.nmt.edu/tcc/help/pubs/colortheory/web/hsv.html for an explanation of HSV color.
        final float[] hsvValues = new float[3];

        waitForStart();

        while(opModeIsActive()) {
            NormalizedRGBA rgba = color.getNormalizedColors();
            // Update the hsvValues array by passing it to Color.colorToHSV()
            Color.colorToHSV(rgba.toColor(), hsvValues);

            telemetry.addLine()
                    .addData("Red", "%.3f", rgba.red)
                    .addData("Green", "%.3f", rgba.green)
                    .addData("Blue", "%.3f", rgba.blue);
            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2]);
            telemetry.addData("Alpha", "%.3f", rgba.alpha);

            telemetry.update();
        }
    }
}
