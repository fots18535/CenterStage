package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;

@Autonomous
public class RedBackPixelAuto extends RedBackAuto{
    public void doLastSteps()
    {
        hunk.chaChaRealSmooth(-0.5, 23);
        AprilTagYay april = new AprilTagYay(this, hardware);
        april.initializes();
        april.align(targetTag);
        hunk.lazerAlign();
        hunk.raiseArm();
        sleep(2000);

        hunk.lowerArm();
        hardware.intakeMotor.setPower(0.5);
        sleep(1000);

    }
}
