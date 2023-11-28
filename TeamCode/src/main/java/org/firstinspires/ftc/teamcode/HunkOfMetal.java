package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class HunkOfMetal {
    LinearOpMode mode;


    float ticksPerInch = 1870.57918f;
    float gyroCorrection = -0.04f;
    float slideTicksPerInch = 1870.57918f;

    Hardware hardware;

    public HunkOfMetal(LinearOpMode op, Hardware hw) {
        mode = op;
        hardware = hw;
    }

    public double ramp(double power, long startTime) {
        // ramp for 0.75 seconds
        long t = System.currentTimeMillis() - startTime;
        if (t >= 750) {
            return power;
        } else {
            return power / 750 * t;
        }
    }

    // Positive power slides left
    // Negative power slides right
    public void chaChaRealSmooth(double power, double length) {
        // Reset the encoder to 0
        hardware.perp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        hardware.perp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Slide until encoder ticks are sufficient
        hardware.gyro.reset();
        long startTime = System.currentTimeMillis();
        while (mode.opModeIsActive()) {
            //absolute value of getCurrentPosition()
            int tics = hardware.perp.getCurrentPosition();
            if (tics < 0) {
                tics = tics * -1;
            }

            double rpower = ramp(power, startTime);
            float rightX = -1 * gyroCorrection * (float) hardware.gyro.getAngle();
            hardware.leftBack.setPower(rightX - rpower);
            hardware.leftFront.setPower(rightX + rpower);
            hardware.rightBack.setPower(rightX - rpower);
            hardware.rightFront.setPower(rightX + rpower);

            if (tics > length * slideTicksPerInch) {
                break;
            }
            mode.idle();
        }

        // Turn off motors
        hardware.leftBack.setPower(0);
        hardware.leftFront.setPower(0);
        hardware.rightBack.setPower(0);
        hardware.rightFront.setPower(0);
    }

    public void forward(double power, double length) {
        // Reset the encoder to 0
        hardware.par1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        hardware.par1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        hardware.gyro.reset();
        long startTime = System.currentTimeMillis();

        // Go forward until tics reached
        while (mode.opModeIsActive()) {

            //absolute value of getCurrentPosition()
            int tics = hardware.par1.getCurrentPosition();
            if (tics < 0) {
                tics = tics * -1;
            }
            //telemetry.addData("debug tics", tics);
            //telemetry.addData("debug compare to ", length*ticksPerInch);

            if (tics > length * ticksPerInch) {
                break;
            }

            // Get the angle and adjust the power to correct
            double rpower = ramp(power, startTime);
            float rightX = -1 * gyroCorrection * (float) hardware.gyro.getAngle();
            hardware.leftBack.setPower(rightX - rpower);
            hardware.leftFront.setPower(rightX - rpower);
            hardware.rightBack.setPower(rightX + rpower);
            hardware.rightFront.setPower(rightX + rpower);

            mode.idle();
        }

        stopMotors();
    }

    public void forward(double power, Stopper stopper) {
        hardware.gyro.reset();
        long startTime = System.currentTimeMillis();

        // Go forward until stopper stops
        stopper.begin();
        while (mode.opModeIsActive()) {

            if (stopper.stop()) {
                break;
            }

            // Get the angle and adjust the power to correct
            double rpower = ramp(power, startTime);
            float rightX = -1 * gyroCorrection * (float) hardware.gyro.getAngle();
            hardware.leftBack.setPower(rightX - rpower);
            hardware.leftFront.setPower(rightX - rpower);
            hardware.rightBack.setPower(rightX + rpower);
            hardware.rightFront.setPower(rightX + rpower);

            mode.idle();
        }

        stopMotors();
    }

    public void turnLeft(double howFar, double speed) {
        //gyro.resetWithDirection(Gyro.RIGHT);
        hardware.gyro.reset();
        hardware.leftBack.setPower(-speed);
        hardware.leftFront.setPower(-speed);
        hardware.rightBack.setPower(-speed);
        hardware.rightFront.setPower(-speed);

        // Go forward and park behind the line
        while (mode.opModeIsActive()) {
            if (hardware.gyro.getAngle() >= howFar) { //change
                break;
            }
            mode.telemetry.addData("angle", hardware.gyro.getAngle());
            mode.telemetry.update();

            mode.idle();
        }

        hardware.leftBack.setPower(0);
        hardware.leftFront.setPower(0);
        hardware.rightBack.setPower(0);
        hardware.rightFront.setPower(0);
    }

    public void turnRight(double howFar, double speed) {
        //gyro.resetWithDirection(Gyro.LEFT);
        hardware.gyro.reset();
        hardware.leftBack.setPower(speed);
        hardware.leftFront.setPower(speed);
        hardware.rightBack.setPower(speed);
        hardware.rightFront.setPower(speed);

        // Go forward and park behind the line
        while (mode.opModeIsActive()) {
            if (hardware.gyro.getAngle() <= -howFar) {
                break;
            }
            mode.telemetry.addData("angle", hardware.gyro.getAngle());
            mode.telemetry.update();

            mode.idle();
        }

        hardware.leftBack.setPower(0);
        hardware.leftFront.setPower(0);
        hardware.rightBack.setPower(0);
        hardware.rightFront.setPower(0);
    }

    public int tickYeah(DcMotor motor) {
        int ticks = motor.getCurrentPosition();
        if (ticks >= 0)
            return ticks;
        else {
            return ticks * -1;
        }
    }

    public void stopMotors() {
        hardware.leftFront.setPower(0.0);
        hardware.rightBack.setPower(0.0);
        hardware.rightFront.setPower(0.0);
        hardware.leftBack.setPower(0.0);
    }

    public void raiseArm(){
        hardware.arm.setPower(.5);
        while(!hardware.armStop.isPressed()){}
        hardware.arm.setPower(0);
    }

    public void lowerArm(){
        int start = hardware.arm.getCurrentPosition();
        hardware.arm.setPower(-0.5);
        while(Math.abs(start - hardware.arm.getCurrentPosition()) < 50){}
        hardware.arm.setPower(0);
    }
}



