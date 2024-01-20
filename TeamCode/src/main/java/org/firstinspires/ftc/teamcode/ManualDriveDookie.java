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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.checkerframework.checker.units.qual.min;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class ManualDriveDookie extends LinearOpMode {
    Hardware hardware;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware = new Hardware(this);
        hardware.initialize();

        HunkOfMetal hunk = new HunkOfMetal(this, hardware);

        double power = 0;
        double correction = 0;

        waitForStart();

        int armTargetTics = 0;

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        // STATES:
        //      -1 = at target position
        //      0 = going to ground position
        //      1 = going to vertical position
        //      2 = going to forward most position
        //      3 = lowering lift
        int armTargetState = -1;
        int prevArmTargetState = -1;

        // -1 at target
        // 0 = first
        // 1 = second row
        int slidePos = -1;

        boolean slideButtonReleased = true;
        boolean launchTimerReset = false;
        while (opModeIsActive()) {

//            telemetry.addData("par0", hardware.par0.getCurrentPosition());
//            telemetry.addData("par1", hardware.par1.getCurrentPosition());
//            telemetry.addData("perp", hardware.perp.getCurrentPosition());
//            telemetry.update();

            /*****************************/
            /** Driving Control Section **/
            /*****************************/

            double slowSpeed = 0.4;
            if (gamepad1.left_bumper) {
                slowSpeed = 0.5;
            } else {
                slowSpeed = 1.0                                                           ;
            }
            //Get the input from the game pad controller
            double leftX = -gamepad1.left_stick_x * slowSpeed;
            double leftY = gamepad1.left_stick_y * slowSpeed;
            double rightX = gamepad1.right_stick_x * slowSpeed;
            double rightY = gamepad1.right_stick_y * slowSpeed;

            if(gamepad1.right_bumper) {
                // BOARD ALIGN
                double leftDist = hardware.lazerLeft.getDistance(DistanceUnit.INCH);
                double rightDist = hardware.lazerRight.getDistance(DistanceUnit.INCH);

                // (7, -0.4), (2, 0)
                rightY = -1.0 * returnPower(Math.min(leftDist, rightDist));
                rightX = (leftDist - rightDist) * 0.15 ;
                if(rightX > 0.5) {
                    rightX = 0.5;
                } else if(rightX < -0.5) {
                    rightX = -0.5;
                }
                leftX = 0.0;
                telemetry.addData("rightX", rightX);
                telemetry.addData("rightY", rightY);
                telemetry.update();


                hardware.leftBack.setPower(rightX + rightY + leftX);
                hardware.leftFront.setPower(rightX + rightY - leftX);
                hardware.rightBack.setPower(rightX - rightY + leftX);
                hardware.rightFront.setPower(rightX - rightY - leftX);
            } else {
                // NORMAL CONTROL
                hardware.leftBack.setPower(rightX + rightY + leftX);
                hardware.leftFront.setPower(rightX + rightY - leftX);
                hardware.rightBack.setPower(rightX - rightY + leftX);
                hardware.rightFront.setPower(rightX - rightY - leftX);
            }

            /*****************************/
            /** ARM UP AND DOWN SECTION **/
            /*****************************/

            if(gamepad2.dpad_up){
                armTargetState = 1; // going to vertical position
                prevArmTargetState = 1;
            }else if(gamepad2.dpad_down) {
                armTargetState = 3; // make sure the lift is down
                prevArmTargetState = 3;
            }else if(gamepad2.dpad_right) {
                armTargetState = 2; // going to forward most position
                prevArmTargetState = 2;
            }
            if(gamepad2.left_trigger > 0.1) { // up
                armTargetState = -1;
                hardware.arm.setPower(gamepad2.left_trigger);
                armTargetTics = hardware.arm.getCurrentPosition();
            }

            if(gamepad2.right_trigger > 0.1){ // down
                armTargetState =  -1;
                hardware.arm.setPower(-gamepad2.right_trigger);
                armTargetTics = hardware.arm.getCurrentPosition();
            }

            // Code for target state 0: going to ground position
            // if armTargetState == 0 then
            //    set armTargetTics = 0
            //    start the intake rollers and keep them on until close to target position
            //    turn off the intake rollers when at target
            //    set armTargetState = -1 when at target position
            if(armTargetState == 0)
            {
                armTargetTics = 0;
                if(armTargetTics >= hardware.arm.getCurrentPosition())
                {
                    armTargetState = -1;
                    hardware.intakeMotor.setPower(0);
                    hardware.slide.setPower(0);
                } else {
                    hardware.intakeMotor.setPower(0.5);
                    if(!hardware.armStop.isPressed()) {
                        hardware.slide.setPower(0.3);
                    }
                }
            }

            // Code for target state 1: going to vertical position
            // if armTargetState == 1 then
            //    set armTargetTics = 195
            //    start the intake rollers and keep them on until close to target position
            //    turn off the intake rollers when at target
            //    set armTargetState = -1 when at target position
            if(armTargetState == 1)
            {
                armTargetTics = 195;
                if(armTargetTics - 10 <= hardware.arm.getCurrentPosition())
                {
                    armTargetState = -1;
                    hardware.intakeMotor.setPower(0);
                    hardware.slide.setPower(0);
                } else {
                    hardware.intakeMotor.setPower(-0.5);
                    if(hardware.armStop.isPressed()) {
                        hardware.slide.setPower(-0.2);
                    }
                }
            }

            // Code for target state 2: going to forward most position
            // if armTargetState == 2 then
            //    set armTargetTics = 255
            //    start the intake rollers and keep them on until close to target position
            //    turn off the intake rollers when at target
            //    set armTargetState = -1 when at target position
            if(armTargetState == 2)
            {
                armTargetTics = 255;
                if(armTargetTics <= hardware.arm.getCurrentPosition())
                {
                    armTargetState = -1;
                    hardware.intakeMotor.setPower(0);
                    hardware.slide.setPower(0);
                } else {
                    hardware.intakeMotor.setPower(-0.5);
                    //slideIn();
                }
            }

            // Code for target state 3: lower the lift then switch to state 0
            if(armTargetState == 3)
            {
                if(hardware.armStop.isPressed())
                {
                    hardware.slide.setPower(0);
                    armTargetState = 0;
                    prevArmTargetState =0;
                }
                else
                {
                    hardware.slide.setPower(0.7);
                }
            }

            // Given current position and target position, get arm motor power from power equation
            // scale the power going to the motor based on the difference between current position and target
            // y = mx + b
            // when diff = -30 then power = 0.4; when diff = 0 power = 0; when diff = 30 then power = -0.4
            // points: (-20, 0.4), (0, 0), (20, -0.4)
            hardware.arm.setPower(getArmPower(armTargetTics, hardware.arm.getCurrentPosition()));

            /*****************************/
            /** INTAKE MOTOR SECTION    **/
            /*****************************/
            if(gamepad2.circle){
                hardware.intakeMotor.setPower(1);
            }else if(gamepad2.square){
                hardware.intakeMotor.setPower(-1);
            } else if (armTargetState == -1){
                hardware.intakeMotor.setPower(0);
            }

            /*****************************/
            /** LINEAR SLIDE SECTION    **/
            /*****************************/

            if(prevArmTargetState == 1 || prevArmTargetState == 2) {
                if (gamepad2.cross) {
                    if (slideButtonReleased && slidePos > 0) {
                        slidePos--;
                    }
                    slideButtonReleased = false;
                } else if (gamepad2.triangle) {
                    if (slideButtonReleased && slidePos < 3) {
                        slidePos++;
                        hunk.forward(0.3, 0.75);
                    }
                    slideButtonReleased = false;
                } else {
                    slideButtonReleased = true;
                }

                if (slidePos == 0) {
                    telemetry.addData("slidetarget", 0);
                    if (!hardware.armStop.isPressed()) {
                        hardware.slide.setPower(0.7);
                    } else {
                        hardware.slide.setPower(0.0);
                        hardware.slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                        hardware.slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                        //slidePos = -1;
                    }
                }

                if (slidePos == 1) {
                    telemetry.addData("slidetarget", -1500);
                    if (hardware.slide.getCurrentPosition() > -1500) { //-840
                        hardware.slide.setPower(-0.7); // up
                    } else if (hardware.slide.getCurrentPosition() < -1550) {
                        hardware.slide.setPower(0.7); // down
                    } else {
                        hardware.slide.setPower(0);
                        //slidePos = -1;
                    }
                }

                if (slidePos == 2) {
                    telemetry.addData("slidetarget", -2670);
                    if (hardware.slide.getCurrentPosition() > -2670) {
                        hardware.slide.setPower(-0.7); // up
                    } else if (hardware.slide.getCurrentPosition() < -2720) {
                        hardware.slide.setPower(0.7); // down
                    } else {
                        hardware.slide.setPower(0);
                        //slidePos = -1;
                    }
                }

                if (slidePos == 3) {
                    telemetry.addData("slidetarget", -3840);
                    if (hardware.slide.getCurrentPosition() > -3840) {
                        hardware.slide.setPower(-0.7); // up
                    } else if (hardware.slide.getCurrentPosition() < -3890) {
                        hardware.slide.setPower(0.7); // down
                    } else {
                        hardware.slide.setPower(0);
                        //slidePos = -1;
                    }
                }

            } else {
                slidePos = 0;
                if(gamepad2.cross && !hardware.armStop.isPressed()){
                    // down
                    hardware.slide.setPower(0.7);
                }else if(gamepad2.triangle){
                    // up
                    hardware.slide.setPower(-0.7);
                }else if (armTargetState == -1){
                    hardware.slide.setPower(0);
                }
            }

            telemetry.addData("slidePos", slidePos);
            telemetry.addData("slide", hardware.slide.getCurrentPosition());


            /*****************************/
            /** AIRPLANE SECTION        **/
            /*****************************/
            if(gamepad2.right_bumper) {
                if(!launchTimerReset){
                    timer.reset();
                    launchTimerReset = true;
                }
                if(timer.seconds() > 2){
                    hardware.airplane.setPosition(1);
                }

            }
            else {
                launchTimerReset = false;
                hardware.airplane.setPosition(0);
            }

            if(gamepad1.dpad_up){
                hardware.hang.setPower(0.5);
            }

            if(gamepad1.dpad_down){
                hardware.hang.setPower(-0.5);
            }

            if(!gamepad1.dpad_up && !gamepad1.dpad_down){
                hardware.hang.setPower(0.0);

            }

            if(gamepad1.triangle) {
                hardware.leftHang.setPosition(0);
                hardware.rightHang.setPosition(1);
            }else{
                hardware.leftHang.setPosition(1);
                hardware.rightHang.setPosition(0);
            }
            telemetry.update();
        }
    }



    private double getArmPower(int targetPositon, int currentPosition){
        int difference = currentPosition - targetPositon;
        //double y = -.013333333 * difference;
        double y = -.02 * difference;
        if(y < -0.45){
            y = -0.45;
        }
        if(y > 0.45) {
            y = 0.45;
        }

        // if the arm is going down limit the power to 0.1
        if(difference > 0 && hardware.arm.getCurrentPosition() < 180 && y > 0.1) {
            y = 0.1;
        }

        if(difference < 0 && hardware.arm.getCurrentPosition() > 190 && y < -0.1)
        {
            y = -0.1;/**/
        }

        return y;
    }

    public double returnPower(double distance)
    {
        double power = distance * -0.062 + 0.031;
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

    private void slideIn() {
        if(!hardware.armStop.isPressed()) {
            hardware.slide.setPower(0.5);
        } else {
            hardware.slide.setPower(0.5);
        }
    }
}