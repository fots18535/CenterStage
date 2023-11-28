package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.HunkOfMetal;
import org.firstinspires.ftc.teamcode.RedStopper;

@Autonomous
public class DriveToRedTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        NormalizedColorSensor color = hardwareMap.get(NormalizedColorSensor.class, "color1");
        RedStopper stopper = new RedStopper(color);
        Hardware hardware = new Hardware(this);
        hardware.initialize();
        HunkOfMetal hunk = new HunkOfMetal(this, hardware);


        waitForStart();

        hunk.forward(-0.5, stopper);
    }
}
