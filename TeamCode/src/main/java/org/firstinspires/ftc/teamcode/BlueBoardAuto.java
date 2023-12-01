package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.vision.AprilTagYay;
import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;

@Autonomous
public class BlueBoardAuto extends LinearOpMode{

        @Override
        public void runOpMode() throws InterruptedException {
                Hardware hardware = new Hardware(this);
                hardware.initialize();

                // blue HSV limits
                double[] minHsv = {1,2,3};
                double[] maxHsv = {1,2,3};

                // default target
                int targetTag = 2;

                //figure out where the icon is located on the field.
                Detector locationId = new Detector(this);
                locationId.start();
                locationId.setHsvLimits(minHsv, maxHsv);
                sleep(2000);
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

                HunkOfMetal hunk = new HunkOfMetal(this, hardware);
                AprilTagYay april = new AprilTagYay(this, hardware);

                waitForStart();
                hunk.forward(-0.5, 23);

        //place pixel on same line of tape as icon
                //if(icon == IconPosition.LEFT){
                      //deliver pixel to left tape line
                        hunk.chaChaRealSmooth(0.5, 11);
                        hunk.turnLeft(90, .5);
                        hunk.forward(-0.5, 11);
//                }else if(icon == IconPosition.CENTER){
//                      //deliver pixel to center tape line
//                        hunk.forward(0.5, 11);
//                        hunk.turnLeft(90, .5);
//                        hunk.forward(0.5, 34);
//
//                }else{
//                      //deliver pixel to right tape line
//                        hunk.chaChaRealSmooth(0.5, 11);
//                        hunk.turnLeft(90, .5);
//                        hunk.forward(0.5, 46);
//

//                }

        // place other pixel on the backboard using april tags that corresponds with the placement on the tapeline.
                //april.initializes();
                //april.align(targetTag);
                hunk.lazerAlign();
                hunk.raiseArm();
                sleep(2000);
        //park in the backstage
                hunk.chaChaRealSmooth(0.5,23);
        }


}
