package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.checkerframework.checker.units.qual.degrees;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Stopper;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AprilTagYay
{
    // Adjust these numbers to suit your robot.
    final double DESIRED_DISTANCE = 8.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.04  ;   //  Forward Speed Control "Gain". eg: Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  eg: Ramp up to 25% power at a 25 degree Yaw error.   (0.25 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  eg: Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private static final int DESIRED_TAG_ID = 2;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private VisionPortal visionPortal;               // Used to manage the video source.
    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
    private LinearOpMode mode;

    private Hardware hardware;

    public AprilTagYay(LinearOpMode op, Hardware hw){
        mode = op;
        hardware = hw;
    }
    public void initializes() {

            // Initialize the Apriltag Detection process
            initAprilTag();

            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

            // Wait for driver to press start
            mode.telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
            mode.telemetry.addData(">", "Touch Play to start OpMode");
            mode.telemetry.update();
        }

    public boolean align(int tagId){
        boolean navigationSuccess = false;
        boolean targetFound = false;    // Set to true when an AprilTag target is detected
        double drive = 0;               // Desired forward power/speed (-1 to +1)
        double strafe = 0;              // Desired strafe power/speed (-1 to +1)
        double turn = 0;                // Desired turning power/speed (-1 to +1)

        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while (mode.opModeIsActive()) {
            desiredTag = null;
            targetFound = false;

            // Step through the list of detected tags and look for a matching tag
            List<AprilTagDetection> currentDetections = aprilTag.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if ((detection.id == tagId)) {
                        // Yes, we want to use this tag.
                        targetFound = true;
                        desiredTag = detection;
                        break;  // don't look any further.
                    } else {
                        // This tag is in the library, but we do not want to track it right now.
                        mode.telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                    }
                } else {
                    // This tag is NOT in the library, so we don't have enough information to track to it.
                    mode.telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
                }
            }

            double rangeError = 0;
            double headingError = 0;
            double yawError = 0;

            // Tell the driver what we see, and what to do.
            if (targetFound) {
//                mode.telemetry.addData("\n>", "HOLD Left-Bumper to Drive to Target\n");
                mode.telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                mode.telemetry.addData("Range", "%5.1f inches", desiredTag.ftcPose.range);
                mode.telemetry.addData("Bearing", "%3.0f degrees", desiredTag.ftcPose.bearing);
                mode.telemetry.addData("Yaw", "%3.0f degrees", desiredTag.ftcPose.yaw);

                // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
                rangeError = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
                headingError = desiredTag.ftcPose.bearing;
                yawError = desiredTag.ftcPose.yaw;

                if(Math.abs(rangeError) < 1.0 && Math.abs(headingError) < 5 && Math.abs(yawError) < 1.0) {
                    navigationSuccess = true;
                    break;
                }

                // Use the speed and turn "gains" to calculate how we want the robot to move.
                drive = -1.0 * Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
                turn = -1.0 * Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN);
                strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

                mode.telemetry.addData("Auto", "Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
                mode.telemetry.update();

                // Apply desired axes motions to the drivetrain.
                moveRobot(drive, strafe, turn);
                mode.sleep(10);
            } else {
                // TODO: should we search for the target for a bit?
                hardware.leftFront.setPower(0);
                hardware.rightFront.setPower(0);
                hardware.leftBack.setPower(0);
                hardware.rightBack.setPower(0);
                mode.sleep(10);
            }

            // when do we want to exit the loop?
            // it's when we are close enough
            // rangeError in inches +- 2 inch
            // headingError in degrees +- 5
            // yawError in degrees +- 5

            if(timer.seconds() > 5 || (targetFound && (rangeError > -2 && rangeError < 2) && (headingError > -5 && headingError < 5) && (yawError > -5 && yawError < 5)))
            {
                navigationSuccess = true;
                break;
            }
        }

        return navigationSuccess;
    }


    /**
     * Move robot according to desired axes motions
     * <p>
     * Positive X is forward
     * <p>
     * Positive Y is strafe left
     * <p>
     * Positive Yaw is counter-clockwise
     */
    public void moveRobot(double x, double y, double yaw) {
        // Calculate wheel powers.

        // rightX = turn = yaw
        // rightY = drive = x
        // leftX = strafe = y

        // if we are driving forward
//        double leftFrontPower    =  yaw + x - y;
//        double rightFrontPower   =  yaw - x - y;
//        double leftBackPower     =  yaw + x + y;
//        double rightBackPower    =  yaw - x + y;

        // since we are driving in reverse...
        double leftFrontPower    =  yaw - x + y;
        double rightFrontPower   =  yaw + x + y;
        double leftBackPower     =  yaw - x - y;
        double rightBackPower    =  yaw + x - y;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        // Send powers to the wheels.
        hardware.leftFront.setPower(leftFrontPower);
        hardware.rightFront.setPower(rightFrontPower);
        hardware.leftBack.setPower(leftBackPower);
        hardware.rightBack.setPower(rightBackPower);
    }

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {
        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // eg: Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        // Create the vision portal by using a builder.
        if (USE_WEBCAM) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(mode.hardwareMap.get(WebcamName.class, "Webcam 1"))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
    private void    setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            mode.telemetry.addData("Camera", "Waiting");
            mode.telemetry.update();
            while (!mode.isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                mode.sleep(20);
            }
            mode.telemetry.addData("Camera", "Ready");
            mode.telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (!mode.isStopRequested())
        {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                mode.sleep(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            mode.sleep(20);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            mode.sleep(20);
        }
    }
}
