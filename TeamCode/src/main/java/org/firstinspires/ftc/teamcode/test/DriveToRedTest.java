package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.teamcode.HunkOfMetal;
import org.firstinspires.ftc.teamcode.RedStopper;

@Autonomous
public class DriveToRedTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensor color = hardwareMap.get(ColorSensor.class, "color1");
        RedStopper stopper = new RedStopper(color);

        HunkOfMetal hunk = new HunkOfMetal(this);
        hunk.initialize();

        waitForStart();

        hunk.forward(-0.5, stopper);
    }
}
