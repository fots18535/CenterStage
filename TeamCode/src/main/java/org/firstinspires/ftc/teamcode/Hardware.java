package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Hardware {
    public DcMotor leftBack;
    public DcMotor leftFront;
    public DcMotor rightBack;
    public DcMotor rightFront;
    public DcMotorEx par0, par1, perp;

    public DcMotor arm;
    public TouchSensor armStop;
    public DcMotor intakeMotor;
    public DcMotor slide;
    public Gyro2 gyro;
    LinearOpMode mode;

    public Servo airplane;

    public Hardware(LinearOpMode op) {
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

        arm = mode.hardwareMap.get(DcMotor.class, "arm");
        armStop = mode.hardwareMap.get(TouchSensor.class, "armStop");
        intakeMotor = mode.hardwareMap.get(DcMotor.class, "intakeMotor");
        slide = mode.hardwareMap.get(DcMotor.class, "slide");
        airplane = mode.hardwareMap.get(Servo.class,"airplane");

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        gyro.startGyro();


    }
}
