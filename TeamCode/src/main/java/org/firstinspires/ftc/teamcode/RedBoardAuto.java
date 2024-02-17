package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;
import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;

@Autonomous
public class RedBoardAuto extends LinearOpMode {

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
                        hunk.forward(-0.5, 3.5);
                        hunk.chaChaRealSmooth(0.5, 7);
                        hunk.forward(0.5, 5);
                        hunk.chaChaRealSmooth(-0.5, 10);
                        hunk.turnRight(85, .5);
                        hunk.chaChaRealSmooth(0.5, 5);
                        hunk.forward(-0.5, 12);
                        parkDistance = 28;
                        // hunk.forward(-0.5, 11);
                } else if (icon == IconPosition.CENTER) {
//                      //deliver pixel to center tape line
                        hunk.forward(-0.5, 8);
                        hunk.forward(0.5, 8);
                        hunk.turnRight(85, .5);
                        hunk.forward(-0.5, 20);
                        parkDistance = 23;
                }
//
                else {
//                      //deliver pixel to right tape line
                        hunk.chaChaRealSmooth(-0.5, 6);
                        hunk.forward(0.5, 4);
                        hunk.chaChaRealSmooth(-0.5, 17);
                        hunk.forward(-0.5, 4);
                        hunk.turnRight(90, .5);
                        parkDistance = 15;
//                        hunk.chaChaRealSmooth(0.5, 11);
//                        hunk.turnLeft(90, .5);
//                        hunk.forward(0.5, 46);
//

               }

                        // place other pixel on the backboard using april tags that corresponds with the placement on the tapeline.
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
                       // hunk.forward(.5, 2);

                        //park in the backstage
                        hunk.chaChaRealSmooth(-0.5, parkDistance);


                }
        }

