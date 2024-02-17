package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;

@Autonomous
public class BlueBackPixelAuto extends BlueBackAuto {
    public void doLastSteps()
    {
        hunk.forward(-0.5,14);
        hunk.chaChaRealSmooth(0.5, 23);
        AprilTagYay april = new AprilTagYay(this, hardware);
        april.initializes();
        april.align(targetTag);
        hunk.raiseArm();
        hunk.lazerAlign();
        hunk.raiseSlide();
        sleep(2000);
        //hunk.midArmAndLower();
        hunk.forward(0.5,3);
        hunk.lowerSlide();
        hunk.lowerArm();
        hardware.intakeMotor.setPower(0.5);
    }
}
