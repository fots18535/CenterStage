package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;


public class BlueBoardAuto extends LinearOpMode{

        @Override
        public void runOpMode() throws InterruptedException {
        //figure out where the icon is located on the field.
                Detector locationId = new Detector(this);
                HunkOfMetal hunk = new HunkOfMetal(this);
                locationId.start();
                IconPosition icon = locationId.getPosition();

                hunk.forward(1, 23);

        //place pixel on same line of tape as icon
                if(icon == IconPosition.LEFT){
                      //deliver pixel to left tape line
                        hunk.chaChaRealSmooth(1, 11);
                        hunk.turnLeft(90, .5);
                        hunk.forward(1, 23);
                }else if(icon == IconPosition.CENTER){
                      //deliver pixel to center tape line
                        hunk.forward(1, 11);
                        hunk.turnLeft(90, .5);
                        hunk.forward(1, 34);

                }else{
                      //deliver pixel to right tape line
                        hunk.chaChaRealSmooth(-1, 11);
                        hunk.turnLeft(90, .5);
                        hunk.forward(1, 46);

                }

        // place other pixel on the backboard using april tags that corresponds with the placement on the tapeline.


        //park in the backstage
        }


}
