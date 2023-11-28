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
        int armTargetState = 0;
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
            // TODO: use a variable to keep track of what the target position is
            if(gamepad2.dpad_up) {
                armTargetState = 1; // vertical
            }else if(gamepad2.dpad_down) {
                armTargetState = 0; // down
            }else if(gamepad2.dpad_right) {
                armTargetState = 3; // full forward
                armTargetTics = 257;
            }

            if(armTargetState == 1) {
                // make sure the arm if fully in before raising from ground (state 2)
                if(hardware.armStop.isPressed() || hardware.arm.getCurrentPosition() >= 100) {
                    hardware.slide.setPower(0);
                    armTargetTics = 195;
                    armTargetState = 2;
                } else {
                    hardware.slide.setPower(0.7); // down
                }

            }

            if(armTargetState == 2 && hardware.arm.getCurrentPosition() < 100) {
                // keep the arm in while raising past the wheels
                if(hardware.armStop.isPressed()) {
                    hardware.slide.setPower(0);
                } else {
                    hardware.slide.setPower(0.7); // down
                }
            }

            if(armTargetState == 0) {
                // make sure the arm is fully in before lowering to ground
                if(!hardware.armStop.isPressed()) {
                    armTargetTics = 100;
                    hardware.slide.setPower(0.7); // down
                } else {
                    hardware.slide.setPower(0);
                    armTargetTics = 0;
                    armTargetState = -1;
                }
            }

            if(armTargetState == -1) {
                // keep the arm in while lowering past wheels
                if(hardware.armStop.isPressed()) {
                    hardware.slide.setPower(0);
                } else {
                    hardware.slide.setPower(0.7); // down
                }
            }

            hardware.arm.setPower(getArmPower(hardware.arm.getCurrentPosition(), armTargetTics));

            // TODO: every cycle through the loop check if we are at or near the target
            // TODO: if we are too far then reverse the motor
            // TODO: if we are not far enough keep the motor going
            // TODO: make sure the linear slide is all the way in before going to 0
            // TODO: scale the power going to the motor based on the difference between current position and target
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
            // TODO: need to set encoder limits to not over / under extend the slide
            if(gamepad2.cross && !hardware.armStop.isPressed()){
                // down
                hardware.slide.setPower(0.7);
            }else if(gamepad2.triangle){
                // up
                hardware.slide.setPower(-0.7);
            }else{
                hardware.slide.setPower(0);
            }

            /*****************************/
            /** AIRPLANE SECTION        **/
            /*****************************/
            if(gamepad2.right_bumper)
            {
                hardware.airplane.setPosition(180);
            }

        }
    }

    private double getArmPower(int currentTic, int targetTic) {
        double power = -0.0133 * (currentTic - targetTic);

        if(power < 0) {
            power = Math.max(power, -0.4);
        } else {
            power = Math.min(power, 0.4);
        }

        return power;
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