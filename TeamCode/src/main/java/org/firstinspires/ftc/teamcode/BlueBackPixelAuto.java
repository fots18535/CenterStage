package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;

@Autonomous
public class BlueBackPixelAuto extends BlueBackAuto {
    public void doLastSteps()
    {
        hunk.chaChaRealSmooth(0.5, 23);
        AprilTagYay april = new AprilTagYay(this, hardware);
        april.initializes();
        april.align(targetTag);
        hunk.midArm();
        hunk.raiseSlide();
        hunk.raiseArm();
        hunk.lazerAlign();
        sleep(2000);
        hunk.midArmAndLower();
        hunk.lowerArm();
        hardware.intakeMotor.setPower(0.5);

    }
}
