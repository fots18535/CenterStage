package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;
@TeleOp
public class ColorDetectionTest$$ extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Detector d = new Detector(this);
        d.start();
        IconPosition position;
        waitForStart();
        while(opModeIsActive()){
            position = d.getPosition();

            if (position == IconPosition.LEFT) {
                telemetry.addData("position", "left");
            } else if (position == IconPosition.CENTER) {
                telemetry.addData("position", "center");
            } else if (position == IconPosition.RIGHT) {
                telemetry.addData("position", "right");
            }

            telemetry.update();
        }
    }
}
