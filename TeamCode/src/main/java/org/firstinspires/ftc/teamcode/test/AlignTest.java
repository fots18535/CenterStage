package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.HunkOfMetal;
import org.firstinspires.ftc.teamcode.vision.AprilTagYay;

@Autonomous(group="Tests")
public class AlignTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        HunkOfMetal hunk = new HunkOfMetal(this,hardware);
        hardware.initialize();

        AprilTagYay april = new AprilTagYay(this, hardware);
        april.initializes();

        waitForStart();

        april.align(2);

        //hunk.lazerAlign();
    }
}
