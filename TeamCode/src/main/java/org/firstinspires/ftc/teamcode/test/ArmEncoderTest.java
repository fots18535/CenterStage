package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware;
@TeleOp
public class ArmEncoderTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hardware hardware = new Hardware(this);
        hardware.initialize();
        waitForStart();
        // Reset the encoder to 0
        hardware.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        hardware.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // 208 vertical
        // 257 full

        while(opModeIsActive())
        {
            telemetry.addData("arm encoder",hardware.arm.getCurrentPosition());
            telemetry.update();
        }
    }
}
