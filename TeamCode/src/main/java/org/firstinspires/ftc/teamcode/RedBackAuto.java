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

                // blue HSV limits
                double[] minHsv = {168,136,130};
                double[] maxHsv = {182,214,255};

                // default target
                int targetTag = 5;

                //figure out where the icon is located on the field.
                Detector locationId = new Detector(this);
                locationId.start();
                locationId.setHsvLimits(minHsv, maxHsv);
                sleep(2000);
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

                waitForStart();
                hunk.forward(-0.5, 23);

                int parkDistance = 0;

                //place pixel on same line of tape as icon
                if (icon == IconPosition.LEFT) {
                        //deliver pixel to left tape line
                        hunk.chaChaRealSmooth(0.5, 14);
                        hunk.forward(0.5, 5);
                } else if (icon == IconPosition.CENTER) {
//                      //deliver pixel to center tape line
                        hunk.forward(-0.5, 8);
                        hunk.forward(0.5, 8);
                }
//
                else {
//                      //deliver pixel to right tape line
                        hunk.forward(-0.5, 5);
                        hunk.chaChaRealSmooth(-0.5, 5);
                        hunk.forward(0.5, 4);
               }
                }
        }

