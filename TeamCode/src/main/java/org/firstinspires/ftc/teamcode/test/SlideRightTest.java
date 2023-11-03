package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.HunkOfMetal;

@Autonomous
public class SlideRightTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        HunkOfMetal hunk = new HunkOfMetal(this);
        hunk.initialize();
        waitForStart();
        hunk.chaChaRealSmooth(.5,30);
    }
}


