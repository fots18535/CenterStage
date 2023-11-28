package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.vision.AprilTagYay;

@Autonomous
public class AlignTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        hardware.initialize();

        AprilTagYay april = new AprilTagYay(this, hardware);
        april.initializes();

        waitForStart();

        april.align();

    }
}
