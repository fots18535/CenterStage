package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class EncoderTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        DcMotorEx par0 = hardwareMap.get(DcMotorEx.class, "leftBack");
        DcMotorEx par1 = hardwareMap.get(DcMotorEx.class, "rightBack");
        DcMotorEx perp = hardwareMap.get(DcMotorEx.class, "rightFront");
        par1.setDirection(DcMotorSimple.Direction.REVERSE);
        perp.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("par0", par0.getCurrentPosition());
            telemetry.addData("par1", par1.getCurrentPosition());
            telemetry.addData("perp", perp.getCurrentPosition());
            telemetry.update();
        }
    }
}
