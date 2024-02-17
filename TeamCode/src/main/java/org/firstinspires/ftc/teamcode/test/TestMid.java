package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.HunkOfMetal;

@TeleOp
public class TestMid extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        hardware.initialize();
        HunkOfMetal hunk = new HunkOfMetal(this, hardware);
        waitForStart();

//        hunk.midArmAndRaise();

        while(opModeIsActive()) {
            if(gamepad2.dpad_left) {
                hunk.midArmAndRaise();
            } else if(gamepad2.dpad_right) {
                hunk.midArmAndLower();
            } else if(gamepad2.dpad_up) {
                hunk.raiseArm();
            } else if(gamepad2.dpad_down) {
                hunk.lowerArm();
            }
        }


    }
}
