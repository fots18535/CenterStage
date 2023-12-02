package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.vision.Detector;
import org.firstinspires.ftc.teamcode.vision.IconPosition;

@TeleOp
public class ElementDetectionTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Detector detector = new Detector(this);
        detector.start();

        // blue HSV limits
        double[] minHsv = {168,136,130};
        double[] maxHsv = {182,214,255};

        detector.setHsvLimits(minHsv, maxHsv);

        waitForStart();

        while (opModeIsActive()) {
            IconPosition icon = detector.getPosition();

            switch (icon) {
                case LEFT:
                    telemetry.addData("Position", "Left");
                    break;
                case CENTER:
                    telemetry.addData("Position", "Center");
                    break;
                case RIGHT:
                    telemetry.addData("Position", "Right");
                    break;
                default:
                    telemetry.addData("Position", "unknown");
            }
            telemetry.update();
        }
    }
}
