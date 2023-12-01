package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Hardware;

@TeleOp
public class DistanceSensorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        hardware.initialize();

        waitForStart();
        while (opModeIsActive()) {
            double leftDist = hardware.lazerLeft.getDistance(DistanceUnit.INCH);
            double rightDist = hardware.lazerRight.getDistance(DistanceUnit.INCH);

            // (7, -0.4), (2, 0)
            double leftPower = returnPower(leftDist);
            double rightPower = returnPower(rightDist);

            telemetry.addData("lazerLeft",leftDist);
            telemetry.addData("lazerRight",rightDist);
            telemetry.addData("leftPower",leftPower);
            telemetry.addData("rightPower",rightPower);
            telemetry.update();
        }
    }

    public double returnPower(double distance)
    {
        double power = distance * -0.08 + 0.16;
        if(power < -0.4)
        {
            power = -0.4;
        }
        else if(power > 0)
        {
            power = 0;
        }
        return power;
    }
}
