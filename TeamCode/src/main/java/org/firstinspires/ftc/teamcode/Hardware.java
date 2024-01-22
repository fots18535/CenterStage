package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Hardware {
    public DcMotor leftBack;
    public DcMotor leftFront;
    public DcMotor rightBack;
    public DcMotor rightFront;
    public DcMotorEx par0, par1, perp;

    public DcMotor hang;

    public DcMotor arm;
    public TouchSensor armStop;
    public DcMotor intakeMotor;
    public DcMotor slide;
    public Gyro2 gyro;
    LinearOpMode mode;

    public Servo airplane;
    public Servo leftHang;
    public Servo rightHang;

    public DistanceSensor lazerLeft;
    public DistanceSensor lazerRight;

    public DistanceSensor lazerCenterLeft;
    public DistanceSensor lazerCenterRight;

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

        hang = mode.hardwareMap.get(DcMotor.class, "hang");


//        par0 = mode.hardwareMap.get(DcMotorEx.class, "leftBack");
//        par1 = mode.hardwareMap.get(DcMotorEx.class, "rightBack");
//        perp = mode.hardwareMap.get(DcMotorEx.class, "rightFront");

        perp = mode.hardwareMap.get(DcMotorEx.class, "leftBack");
        par1 = mode.hardwareMap.get(DcMotorEx.class, "rightBack");
        par0 = mode.hardwareMap.get(DcMotorEx.class, "rightFront");

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

        leftHang = mode.hardwareMap.get(Servo.class,"leftHang");
        rightHang = mode.hardwareMap.get(Servo.class,"rightHang");

        arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        lazerLeft = mode.hardwareMap.get(DistanceSensor.class, "lazerLeft");
        lazerRight = mode.hardwareMap.get(DistanceSensor.class, "lazerRight");
        lazerCenterLeft = mode.hardwareMap.get(DistanceSensor.class, "lazerCenterLeft");
        lazerCenterRight = mode.hardwareMap.get(DistanceSensor.class, "lazerCenterRight");

        // Reset the encoder to 0
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        gyro.startGyro();
    }

}
