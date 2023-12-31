package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvWebcam;

import java.util.ArrayList;

public class Detector {
    OpenCvWebcam phoneCam;
    IconDeterminationPipeline pipeline;
    LinearOpMode op;

    public Detector(LinearOpMode op) {
        this.op = op;
    }

    public void start() {
        phoneCam = OpenCvCameraFactory.getInstance().createWebcam(op.hardwareMap.get(WebcamName.class, "Webcam 1"));
        //phoneCam = op.hardwareMap.get(WebcamName.class, "Webcam 1");
        pipeline = new IconDeterminationPipeline();
        phoneCam.setPipeline(pipeline);

        // We set the viewport policy to optimized view so the preview doesn't appear 90 deg
        // out when the RC activity is in portrait. We do our actual image processing assuming
        // landscape orientation, though.
        phoneCam.setViewportRenderingPolicy(OpenCvCamera.ViewportRenderingPolicy.OPTIMIZE_VIEW);

        phoneCam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                phoneCam.startStreaming(640,480, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });
    }

    public void stop() {
        phoneCam.stopStreaming();
        phoneCam.closeCameraDevice();
    }

    public IconPosition getPosition() {
        return pipeline.getAnalysis();
    }

    public void setHsvLimits(double[] min, double[] max){
        pipeline.setHsvLimits(min, max);
    }
    public static class IconDeterminationPipeline extends OpenCvPipeline
    {

        /*
         * Example of how points A and B work to define a rectangle
         *
         *   ------------------------------------
         *   | (0,0) Point A                    |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                                  |
         *   |                  Point B (70,50) |
         *   ------------------------------------
         *
         */

        Point lu;
        Point ll;
        Point mu;
        Point ml;
        Point ru;
        Point rl;

        // HSV threshold values
        Scalar lower = new Scalar(77,169,75);
        Scalar upper = new Scalar(140,243,255);

        /*
         * Working variables
         */
        Mat left, mid, right;
        Mat hsv = new Mat();
        Mat mask = new Mat();
        Mat mask3chan = new Mat();
        Mat contoursOnPlainImageMat = new Mat();
        int avg1, avg2, avg3;
        Mat output = new Mat();

        Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(6, 6));

        static final Scalar BLUE = new Scalar(0, 0, 255);
        boolean drawRegions = true;

        // Volatile since accessed by OpMode thread w/o synchronization
        private volatile IconPosition position = IconPosition.LEFT;

        // TODO: add method to set the upper and lower threshold values
        public void setHsvLimits(double[] min, double[] max){
            lower = new Scalar(min);
            upper = new Scalar(max);
        }

        @Override
        public void init(Mat firstFrame)
        {
            Imgproc.cvtColor(firstFrame, hsv, Imgproc.COLOR_RGB2HSV);
            Core.inRange(hsv, lower, upper, mask);
            Imgproc.cvtColor(mask, mask3chan, Imgproc.COLOR_GRAY2RGB);

            int w = firstFrame.width();
            int h = firstFrame.height();

            lu = new Point(0,0);
            ll = new Point(w/3,h);
            mu = new Point(w/3,0);
            ml = new Point(2 * w / 3, h);
            ru = new Point(2 * w / 3, 0);
            rl = new Point(w,h);

            /*
             * Submats are a persistent reference to a region of the parent
             * buffer. Any changes to the child affect the parent, and the
             * reverse also holds true.
             */

            left = mask.submat(new Rect(lu,ll));
            mid = mask.submat(new Rect(mu,ml));
            right = mask.submat(new Rect(ru,rl));

        }

        @Override
        public Mat processFrame(Mat input) {
            Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

            Core.inRange(hsv, lower, upper, mask);
            Imgproc.cvtColor(mask, mask3chan, Imgproc.COLOR_GRAY2RGB);

            avg1 = (int) Core.mean(left).val[0];
            avg2 = (int) Core.mean(mid).val[0];
            avg3 = (int) Core.mean(right).val[0];

            avgs[0] = avg1;
            avgs[1] = avg2;
            avgs[2] = avg3;

            if(avg1 > avg2 && avg1 > avg3)
            {
                position = IconPosition.LEFT;
            }
            else if(avg2 > avg1 && avg2 > avg3)
            {
                position = IconPosition.CENTER;
            }
            else
            {
                position = IconPosition.RIGHT;
            }

            if(drawRegions) {
                Imgproc.rectangle(
                        mask3chan, // Buffer to draw on
                        lu, // First point which defines the rectangle
                        ll, // Second point which defines the rectangle
                        BLUE, // The color the rectangle is drawn in
                        2); // Thickness of the rectangle lines
                Imgproc.rectangle(mask3chan, mu, ml, BLUE, 2);
                Imgproc.rectangle(mask3chan, ru, rl, BLUE, 2);
            }

            return mask3chan;
        }

        /*
         * Call this from the OpMode thread to obtain the latest analysis
         */
        public IconPosition getAnalysis()
        {
            return position;
        }

        int[] avgs = {0,0,0};
        public int[] getAvgs() {
            return avgs;
        }
    }
}
