package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class HunkOfMetal {
    DcMotor leftBack;
    DcMotor leftFront; 
    DcMotor rightBack;
    DcMotor rightFront;
    DcMotorEx par0, par1, perp;

    Gyro2 gyro;
    LinearOpMode mode;

    float ticksPerInch = 1870.57918f;
    float gyroCorrection = -0.04f;
    float slideTicksPerInch = 1870.57918f;

    public HunkOfMetal(LinearOpMode op) {
        mode = op;
    }

    public void initialize() {
        BNO055IMU imu = mode.hardwareMap.get(BNO055IMU.class, "imu");
        gyro = new Gyro2(imu, mode);

        leftBack = mode.hardwareMap.get(DcMotor.class, "leftBack");
        leftFront = mode.hardwareMap.get(DcMotor.class, "leftFront");
        rightBack = mode.hardwareMap.get(DcMotor.class, "rightBack");
        rightFront = mode.hardwareMap.get(DcMotor.class, "rightFront");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);

        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        par0 = mode.hardwareMap.get(DcMotorEx.class, "leftBack");
        par1 = mode.hardwareMap.get(DcMotorEx.class, "rightBack");
        perp = mode.hardwareMap.get(DcMotorEx.class, "rightFront");
        par0.setDirection(DcMotorSimple.Direction.REVERSE);
        par1.setDirection(DcMotorSimple.Direction.REVERSE);
        perp.setDirection(DcMotorSimple.Direction.REVERSE);

        // Reset the encoder to 0
        par0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        par0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        par1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        par1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        perp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        perp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gyro.startGyro();
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
        perp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        perp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        // Slide until encoder ticks are sufficient
        gyro.reset();
        long startTime = System.currentTimeMillis();
        while (mode.opModeIsActive()) {
            //absolute value of getCurrentPosition()
            int tics = perp.getCurrentPosition();
            if (tics < 0) {
                tics = tics * -1;
            }

            double rpower = ramp(power, startTime);
            float rightX = -1 * gyroCorrection * (float) gyro.getAngle();
            leftBack.setPower(rightX - rpower);
            leftFront.setPower(rightX + rpower);
            rightBack.setPower(rightX - rpower);
            rightFront.setPower(rightX + rpower);

            if (tics > length * slideTicksPerInch) {
                break;
            }
            mode.idle();
        }

        // Turn off motors
        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void forward(double power, double length) {
        // Reset the encoder to 0
        par0.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // Tells the motor to run until we turn it off
        par0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gyro.reset();
        long startTime = System.currentTimeMillis();

        // Go forward until tics reached
        while (mode.opModeIsActive()) {

            //absolute value of getCurrentPosition()
            int tics = par0.getCurrentPosition();
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
            float rightX = -1 * gyroCorrection * (float) gyro.getAngle();
            leftBack.setPower(rightX - rpower);
            leftFront.setPower(rightX - rpower);
            rightBack.setPower(rightX + rpower);
            rightFront.setPower(rightX + rpower);

            mode.idle();
        }

        stopMotors();
    }

    public void forward(double power, Stopper stopper) {
        gyro.reset();
        long startTime = System.currentTimeMillis();

        // Go forward until stopper stops
        stopper.begin();
        while (mode.opModeIsActive()) {

            if (stopper.stop()) {
                break;
            }

            // Get the angle and adjust the power to correct
            double rpower = ramp(power, startTime);
            float rightX = -1 * gyroCorrection * (float) gyro.getAngle();
            leftBack.setPower(rightX - rpower);
            leftFront.setPower(rightX - rpower);
            rightBack.setPower(rightX + rpower);
            rightFront.setPower(rightX + rpower);

            mode.idle();
        }

        stopMotors();
    }

    public void turnLeft(double howFar, double speed) {
        //gyro.resetWithDirection(Gyro.RIGHT);
        gyro.reset();
        leftBack.setPower(-speed);
        leftFront.setPower(-speed);
        rightBack.setPower(-speed);
        rightFront.setPower(-speed);

        // Go forward and park behind the line
        while (mode.opModeIsActive()) {
            if (gyro.getAngle() >= howFar) { //change
                break;
            }
            mode.telemetry.addData("angle", gyro.getAngle());
            mode.telemetry.update();

            mode.idle();
        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
    }

    public void turnRight(double howFar, double speed) {
        //gyro.resetWithDirection(Gyro.LEFT);
        gyro.reset();
        leftBack.setPower(speed);
        leftFront.setPower(speed);
        rightBack.setPower(speed);
        rightFront.setPower(speed);

        // Go forward and park behind the line
        while (mode.opModeIsActive()) {
            if (gyro.getAngle() <= -howFar) {
                break;
            }
            mode.telemetry.addData("angle", gyro.getAngle());
            mode.telemetry.update();

            mode.idle();
        }

        leftBack.setPower(0);
        leftFront.setPower(0);
        rightBack.setPower(0);
        rightFront.setPower(0);
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
        leftFront.setPower(0.0);
        rightBack.setPower(0.0);
        rightFront.setPower(0.0);
        leftBack.setPower(0.0);
    }
}



