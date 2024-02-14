package org.firstinspires.ftc.teamcode;


import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
        long measuredTime = System.currentTimeMillis();
        int measuredTics = 0;
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
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

            if (tics > length * slideTicksPerInch || timer.seconds() > 9) {
                break;
            }

            // If we are trying to move but are not then throw an exception
            if(System.currentTimeMillis() - measuredTime > 1000)
            {
                if(measuredTics < 1000)
                {
                    stopMotors();
                    throw new RuntimeException("We are stuck");
                }
                measuredTics = 0;
                measuredTime = System.currentTimeMillis();
            } else {
                measuredTics+= tics;
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
        long measuredTime = System.currentTimeMillis();
        int measuredTics = 0;

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

            // If we are trying to move but are not then throw an exception
            if(System.currentTimeMillis() - measuredTime > 1000)
            {
                if(measuredTics < 1000)
                {
                    stopMotors();
                    throw new RuntimeException("We are stuck");
                }
                measuredTics = 0;
                measuredTime = System.currentTimeMillis();
            } else {
                measuredTics+= tics;
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

   public void raiseArm()
   {
       int armTargetTics = 250;
       while(mode.opModeIsActive())
       {
           if(armTargetTics-15 <= hardware.arm.getCurrentPosition())
           {
               break;
           }
           hardware.intakeMotor.setPower(-0.5);
           hardware.arm.setPower(getArmPower(armTargetTics, hardware.arm.getCurrentPosition()));
           hardware.slide.setPower(-0.2);
       }
       hardware.intakeMotor.setPower(0);
       hardware.slide.setPower(0);
   }

   public void lowerArm()
   {
       int armTargetTics = 0;
       int tolerance = 10;
       while(mode.opModeIsActive())
       {
           if(armTargetTics + tolerance >= hardware.arm.getCurrentPosition())
           {
               break;
           }
           hardware.intakeMotor.setPower(0.5);
           hardware.arm.setPower(getArmPower(armTargetTics, hardware.arm.getCurrentPosition()));
           if(hardware.armStop.isPressed())
           {
               hardware.slide.setPower(0);
           }
           else {
               hardware.slide.setPower(0.3);
           }
       }
       hardware.intakeMotor.setPower(0);
       hardware.slide.setPower(0);
   }

    private double getArmPower(int targetPositon, int currentPosition){
        int difference = currentPosition - targetPositon;
        double y = -.013333333 * difference;
        if(y < -0.4){
            y = -0.4;
        }
        if(y > 0.4) {
            y = 0.4;
        }

        // if the arm is going down limit the power to 0.1
        if(difference > 0 && hardware.arm.getCurrentPosition() < 180 && y > 0.1) {
            y = 0.1;
        }

        if(difference < 0 && hardware.arm.getCurrentPosition() > 190 && y < -0.1)
        {
            y = -0.1;
        }

        return y;
    }

    public void lazerAlign()
    {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while(mode.opModeIsActive()){
            double leftDist = hardware.lazerLeft.getDistance(DistanceUnit.INCH);
            double rightDist = hardware.lazerRight.getDistance(DistanceUnit.INCH);
            double centerLeftDist = hardware.lazerCenterLeft.getDistance(DistanceUnit.INCH);
            double centerRightDist = hardware.lazerCenterRight.getDistance(DistanceUnit.INCH);

            if(timer.seconds() > 4 || Math.min(leftDist,rightDist) < 1)
            {
                break;
            }

            // (7, -0.4), (0.5, 0)
            double rightY = -1.0 * returnPower(Math.min(leftDist, rightDist));
            double rightX = 0;
            if(Math.max(leftDist, rightDist) - Math.min(leftDist, rightDist) < 6)
            {
                rightX = (leftDist - rightDist) * 0.15 ;
            }
            if(rightX > 0.5) {
                rightX = 0.5;
            } else if(rightX < -0.5) {
                rightX = -0.5;
            }
            double leftX = 0.0;

            hardware.leftBack.setPower(rightX + rightY + leftX);
            hardware.leftFront.setPower(rightX + rightY - leftX);
            hardware.rightBack.setPower(rightX - rightY + leftX);
            hardware.rightFront.setPower(rightX - rightY - leftX);
        }

        stopMotors();
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
}



