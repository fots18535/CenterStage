package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(group="Tests")
public class TouchTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        TouchSensor touch = hardwareMap.get(TouchSensor.class, "armStop");
        waitForStart();
        while (opModeIsActive()) {
           telemetry.addData("Pressed", touch.isPressed());
           telemetry.update();
        }
    }
}
