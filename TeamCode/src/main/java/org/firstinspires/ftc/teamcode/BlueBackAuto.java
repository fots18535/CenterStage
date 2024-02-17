package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;
import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;

@Autonomous
public class BlueBackAuto extends LinearOpMode {
    HunkOfMetal hunk;
    Hardware hardware;
     int targetTag = 2;
    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(this);
        hardware.initialize();

        // blue HSV limits
        double[] minHsv = {108,93,88};
        double[] maxHsv = {126,255,255};

        //figure out where the icon is located on the field.
        Detector locationId = new Detector(this);
        locationId.start();
        locationId.setHsvLimits(minHsv, maxHsv);
        sleep(2000);


        waitForStart();
        IconPosition icon = locationId.getPosition();
        switch (icon) {
            case LEFT:
                telemetry.addData("Position", "Left");
                targetTag = 1;
                break;
            case CENTER:
                telemetry.addData("Position", "Center");
                targetTag = 2;
                break;
            case RIGHT:
                telemetry.addData("Position", "Right");
                targetTag = 3;
                break;
            default:
                telemetry.addData("Position", "unknown");
        }
        telemetry.update();
        locationId.stop();

        hunk = new HunkOfMetal(this, hardware);
        AprilTagYay april = new AprilTagYay(this, hardware);
        hunk.forward(-0.5, 23);

        //place pixel on same line of tape as icon
        if (icon == IconPosition.LEFT) {
            //deliver pixel to left tape line
            hunk.chaChaRealSmooth(0.5, 13);
            hunk.forward(-0.5, 4);
            hunk.forward(0.5, 4);
            hunk.chaChaRealSmooth(-0.5,13);
            hunk.forward(-0.5,24);
            hunk.turnLeft(90,0.5);
            hunk.forward(-0.5,64);
        } else if (icon == IconPosition.CENTER) {
            hunk.forward(-0.5, 8);
            hunk.forward(0.5, 8);
            hunk.chaChaRealSmooth(-0.5,19);
            hunk.forward(-0.8,27);
            hunk.turnLeft(85,0.5);
            hunk.forward(-0.8,85);
        } else {
//                      //deliver pixel to right tape line
            hunk.forward(-0.5, 6);
            hunk.chaChaRealSmooth(-0.5, 7);
            hunk.forward(0.5, 5);
            hunk.chaChaRealSmooth(0.5,31);
            hunk.forward(-0.5,24);
            hunk.turnLeft(85,0.5);
            hunk.forward(-0.5, 35);
        }
        doLastSteps();
    }

    public void doLastSteps()
    {
        hunk.forward(-0.5,20);
        hunk.raiseArm();
        sleep(1000);
        hunk.lowerArm();
        hardware.intakeMotor.setPower(0.5);
        sleep(1000);
    }
}