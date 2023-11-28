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
        hardware.arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        int armTargetTics = 0;

        // STATES:
        //      -1 = at target position
        //      0 = going to ground position
        //      1 = going to vertical position
        //      2 = going to forward most position
        int armTargetState = -1;
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

            /*****************************/
            /** ARM UP AND DOWN SECTION **/
            /*****************************/

            if(gamepad2.dpad_up){
                armTargetState = 1; // going to vertical position
            }else if(gamepad2.dpad_down) {
                armTargetState = 0; // going to ground position
            }else if(gamepad2.dpad_right) {
                armTargetState = 2; // going to forward most position
            }

            // TODO: code for target state 0: going to ground position
            // if armTargetState == 0 then
            //    set armTargetTics = 0
            //    start the intake rollers and keep them on until close to target position
            //    turn off the intake rollers when at target
            //    set armTargetState = -1 when at target position

            // TODO: code for target state 1: going to vertical position
            // if armTargetState == 1 then
            //    set armTargetTics = 195
            //    start the intake rollers and keep them on until close to target position
            //    turn off the intake rollers when at target
            //    set armTargetState = -1 when at target position

            // TODO: code for target state 2: going to forward most position
            // if armTargetState == 2 then
            //    set armTargetTics = 255
            //    start the intake rollers and keep them on until close to target position
            //    turn off the intake rollers when at target
            //    set armTargetState = -1 when at target position

            // TODO: given current position and target position, get arm motor power from power equation
            //       scale the power going to the motor based on the difference between current position and target
            //       y = mx + b
            //       when diff = -30 then power = 0.4; when diff = 0 power = 0; when diff = 30 then power = -0.4
            //       points: (-30, 0.4), (0, 0), (30, -0.4) - what is the equation?

            /*****************************/
            /** INTAKE MOTOR SECTION    **/
            /*****************************/
            if(gamepad2.circle){
                hardware.intakeMotor.setPower(1);
            }else if(gamepad2.square){
                hardware.intakeMotor.setPower(-1);
            }else{
                hardware.intakeMotor.setPower(0);
            }

            /*****************************/
            /** LINEAR SLIDE SECTION    **/
            /*****************************/
            // TODO: need to set encoder limits and/or button to not over / under extend the slide
            if(gamepad2.cross){
                hardware.slide.setPower(0.7);
            }else if(gamepad2.triangle){
                hardware.slide.setPower(-0.7);
            }else{
                hardware.slide.setPower(0);
            }

            /*****************************/
            /** AIRPLANE SECTION        **/
            /*****************************/
            if(gamepad2.right_bumper) {
                // TODO: servo range is 0 - 1
                hardware.airplane.setPosition(180);
            }
            // TODO: what is the servo position when the right bumper is not pushed?
        }
    }
}