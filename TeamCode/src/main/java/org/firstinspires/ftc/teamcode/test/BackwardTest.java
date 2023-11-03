package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HunkOfMetal;

@Autonomous
public class BackwardTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        HunkOfMetal hunk = new HunkOfMetal(this);
        hunk.initialize();
        waitForStart();
        hunk.forward(-0.5,30);
    }
}