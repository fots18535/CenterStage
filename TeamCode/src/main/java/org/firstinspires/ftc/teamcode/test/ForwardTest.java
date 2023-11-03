package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.HunkOfMetal;

@Autonomous
public class ForwardTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        HunkOfMetal hunk = new HunkOfMetal(this);
        hunk.initialize();
        waitForStart();
        hunk.forward(.5,30);
    }
}
