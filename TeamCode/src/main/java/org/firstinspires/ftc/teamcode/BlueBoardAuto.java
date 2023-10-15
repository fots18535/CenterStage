package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;


public class BlueBoardAuto extends LinearOpMode{

        @Override
        public void runOpMode() throws InterruptedException {
        //figure out where the icon is located on the field.
                Detector locationId = new Detector(this);
                HunkOfMetal move = new HunkOfMetal(this);
                locationId.start();
                IconPosition icon = locationId.getPosition();

        //place pixel on same line of tape as icon
                if(icon == IconPosition.LEFT){
                      //deliver pixel to left tape line
                }else if(icon == IconPosition.CENTER){
                      //deliver pixel to center tape line
                }else{
                      //deliver pixel to right tape line
              }

        // place other pixel on the backboard using april tags that corresponds with the placement on the tapeline.
               move.turnRight(90, .5);


        //park in the backstage
        }


}
