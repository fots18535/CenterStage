package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.checkerframework.checker.units.qual.min;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class ManualDriveDookie extends LinearOpMode {
    Hardware hardware;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(this);
        hardware.initialize();

        double power = 0;
        double correction = 0;

        waitForStart();

        // Reset the encoder to 0
        hardware.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        hardware.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (opModeIsActive()) {

            telemetry.addData("par0", hardware.par0.getCurrentPosition());
            telemetry.addData("par1", hardware.par1.getCurrentPosition());
            telemetry.addData("perp", hardware.perp.getCurrentPosition());
            telemetry.update();

            /*****************************/
            /** Driving Control Section **/
            /*****************************/

            double slowSpeed = 0.4;
            if (gamepad1.left_bumper) {
                slowSpeed = 0.5;
            } else {
                slowSpeed = 0.8;
            }
            //Get the input from the game pad controller
            double leftX = -gamepad1.left_stick_x * slowSpeed;
            double leftY = gamepad1.left_stick_y * slowSpeed;
            double rightX = gamepad1.right_stick_x * slowSpeed;
            double rightY = gamepad1.right_stick_y * slowSpeed;
            double distance = 7.0;


            hardware.leftBack.setPower(rightX + rightY + leftX);
            hardware.leftFront.setPower(rightX + rightY - leftX);
            hardware.rightBack.setPower(rightX - rightY + leftX);
            hardware.rightFront.setPower(rightX - rightY - leftX);

    //touch sensor for the arm
            if(gamepad2.dpad_up){
                armSetter(195);
            }else if(gamepad2.dpad_down) {
                armSetter(0);
            }else if(gamepad2.dpad_right)
            {
                armSetter(257);
            }else{
                hardware.arm.setPower(0);
            }

    //intake motor
            if(gamepad2.circle){
                hardware.intakeMotor.setPower(1);
            }else if(gamepad2.square){
                hardware.intakeMotor.setPower(-1);
            }else{
                hardware.intakeMotor.setPower(0);
            }

    //linear slide motor
            if(gamepad2.cross){
                hardware.slide.setPower(0.7);
            }else if(gamepad2.triangle){
                hardware.slide.setPower(-0.7);
            }else{
                hardware.slide.setPower(0);
            }

      //airplane
            if(gamepad2.right_bumper)
            {
                hardware.airplane.setPosition(180);
            }

        }
    }

    private void armSetter(int tics)
    {
        int current = hardware.arm.getCurrentPosition();
        int difference = tics - current;
        if(difference < 0)
        {
            hardware.arm.setPower(-0.4);
            while(opModeIsActive() && hardware.arm.getCurrentPosition() > tics)
            {
                telemetry.addData("arm pause",hardware.arm.getCurrentPosition());
                telemetry.addData("tics",tics);
                telemetry.update();
            }
        }
        else
        {
            hardware.arm.setPower(0.4);
            while(opModeIsActive() && hardware.arm.getCurrentPosition() < tics)
            {
                telemetry.addData("arm pause",hardware.arm.getCurrentPosition());
                telemetry.addData("tics",tics);
                telemetry.update();
            }
        }
        hardware.arm.setPower(0);
    }
}