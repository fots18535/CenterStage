package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;
import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;

@Autonomous
public class RedBackAuto extends LinearOpMode {

        @Override
        public void runOpMode() throws InterruptedException {
                Hardware hardware = new Hardware(this);
                hardware.initialize();

                // red HSV limits
                double[] minHsv = {153,135,0};
                double[] maxHsv = {191,228,255};

                // default target
                int targetTag = 5;

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
                                targetTag = 4;
                                break;
                        case CENTER:
                                telemetry.addData("Position", "Center");
                                targetTag = 5;
                                break;
                        case RIGHT:
                                telemetry.addData("Position", "Right");
                                targetTag = 6;
                                break;
                        default:
                                telemetry.addData("Position", "unknown");
                }
                telemetry.update();
                locationId.stop();

                HunkOfMetal hunk = new HunkOfMetal(this, hardware);
                AprilTagYay april = new AprilTagYay(this, hardware);
                hunk.forward(-0.5, 23);

                int parkDistance = 0;

                //place pixel on same line of tape as icon
                if (icon == IconPosition.LEFT) {
                        //deliver pixel to left tape line
                        hunk.forward(-0.5, 6);
                        hunk.chaChaRealSmooth(0.5, 13);
                        hunk.forward(0.5, 3);
                        hunk.chaChaRealSmooth(-0.5, 37.5);
                        hunk.forward(-0.5, 24);
                        hunk.turnRight(90,0.5);
                        hunk.forward(-0.5,56);
                } else if (icon == IconPosition.CENTER) {
//                      //deliver pixel to center tape line
                        hunk.forward(-0.5, 8);
                        hunk.forward(0.5, 8);
                        hunk.chaChaRealSmooth(0.5, 16);
                        hunk.forward(-0.5, 26);
                        hunk.turnRight(90,0.5);
                        hunk.forward(-0.5,100);
                }
//
                else {
//                      //deliver pixel to right tape line
                        hunk.forward(-0.5, 4);
                        hunk.chaChaRealSmooth(-0.5, 6);
                        hunk.forward(0.5, 3);
                        hunk.chaChaRealSmooth(0.5, 16);
                        hunk.forward(-0.5, 24);
                        hunk.turnRight(90,0.5);
                        hunk.forward(-0.5,90);
               }
                }
        }

