package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.HunkOfMetal;

@Autonomous
public class BackwardTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        hardware.initialize();

        HunkOfMetal hunk = new HunkOfMetal(this, hardware);
        waitForStart();
        hunk.forward(-0.5,30);
    }
}