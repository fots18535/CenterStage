package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Hardware;

@TeleOp
public class EncoderTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        Hardware hardware = new Hardware(this);
        hardware.initialize();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("par0", hardware.par0.getCurrentPosition());
            telemetry.addData("par1", hardware.par1.getCurrentPosition());
            telemetry.addData("perp", hardware.perp.getCurrentPosition());
            telemetry.update();
        }
    }
}
