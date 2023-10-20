package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.checkerframework.checker.units.qual.min;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class ManualDriveDookie extends LinearOpMode {

    DcMotor leftBack;
    DcMotor leftFront;
    DcMotor rightBack;
    DcMotor rightFront;
    DcMotor arm;
    TouchSensor armStop;
    DcMotor intakeMotor;
    DcMotor slide;

    @Override
    public void runOpMode() throws InterruptedException {
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        arm = hardwareMap.get(DcMotor.class, "arm");
        armStop = hardwareMap.get(TouchSensor.class, "armStop");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        slide = hardwareMap.get(DcMotor.class, "slide");

        // Reset the encoder to 0
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Stops coasting
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        double power = 0;
        double correction = 0;

        waitForStart();

        while (opModeIsActive()) {

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
            double leftX = gamepad1.left_stick_x * slowSpeed;
            double leftY = gamepad1.left_stick_y * slowSpeed;
            double rightX = -gamepad1.right_stick_x * slowSpeed;
            double rightY = gamepad1.right_stick_y * slowSpeed;
            double distance = 7.0;


            leftBack.setPower(rightX + rightY + leftX);
            leftFront.setPower(rightX + rightY - leftX);
            rightBack.setPower(rightX - rightY + leftX);
            rightFront.setPower(rightX - rightY - leftX);

    //touch sensor for the arm
            if(gamepad2.dpad_up && !armStop.isPressed()){
                arm.setPower(0.5);
            }else if(gamepad2.dpad_down) {
                arm.setPower(-0.5);
            }else{
                arm.setPower(0);
            }

    //intake motor
            if(gamepad2.circle){
                intakeMotor.setPower(0.5);
            }else if(gamepad2.square){
                intakeMotor.setPower(-0.5);
            }else{
                intakeMotor.setPower(0);
            }

    //linear slide motor
            if(gamepad2.cross){
                slide.setPower(0.3);
            }else if(gamepad2.triangle){
                slide.setPower(-0.3);
            }else{
                slide.setPower(0);
            }
        }
    }
}