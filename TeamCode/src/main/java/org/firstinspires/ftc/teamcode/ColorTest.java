package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name="ColorTest", group="Autonomous")
public class ColorTest extends OpMode {
    private Camera camera;
    private CameraController cameraController = new CameraController();
    private Bitmap image;

    @Override
    public void init () {
        camera = cameraController.startCamera();
        telemetry.setAutoClear(false);
        if (camera == null)
        {
            telemetry.addData("NO Cam Found", "");
            telemetry.update();
        }
        else
        {
            telemetry.addData("Cam has been found", "");
            telemetry.update();
        }
    }
    @Override
    public void start () {
        cameraController.stopCameraInSecs(35);
    }
    @Override
    public void loop () {
        if (cameraController.yuvImage != null)
        {
            image = cameraController.convertYuvImageToBitmap(cameraController.yuvImage);
            if (image != null)
            {
                String test = whichSide(image);
                if (test != "right" && test != "center" && test != "left") {
                    telemetry.addData("Side is ", "%s", test);
                    telemetry.update();
                }
                else {
                    telemetry.addData("Side is ", "nada");
                    telemetry.update();
                }
            }
            else
            {
                telemetry.addData("no image", "oof");
                telemetry.update();
            }
        }
        else
        {
            telemetry.addData("image not good", "oof");
            telemetry.update();
            if (cameraController.yuvImage != null)
            {
                telemetry.addData("Image good tho", "");
            }
        }
    }

    public String whichSide (Bitmap picture) {
        int goldRight = 0;
        int goldLeft = 0;
        int goldCenter = 0;
        int heightOfImage = picture.getHeight();
        int widthOfImage = picture.getWidth();
        int maxGoldRed = 255;
        int minGoldRed = 200;
        int maxGoldGreen = 255;
        int minGoldGreen = 200;
        int maxGoldBlue = 142;
        int minGoldBlue = 88;
        for (int i = 0; i < widthOfImage / 3; i++) {
            for (int j = 0; j < heightOfImage; j++) {
                int pixel = picture.getPixel(i, j);
                int red = cameraController.red(pixel);
                int green = cameraController.green(pixel);
                int blue = cameraController.blue(pixel);
                if ((red <= maxGoldRed && red >= minGoldRed) && (green <= maxGoldGreen && green >= minGoldGreen) && (blue <= maxGoldBlue && blue >= minGoldBlue)) {
                    goldLeft += 1;
                }
            }
        }
        for (int i = widthOfImage / 3; i < 2 * (widthOfImage / 3); i++) {
            for (int j = 0; j < heightOfImage; j++) {
                int pixel = picture.getPixel(i, j);
                int red = cameraController.red(pixel);
                int green = cameraController.green(pixel);
                int blue = cameraController.blue(pixel);
                if ((red <= maxGoldRed && red >= minGoldRed) && (green <= maxGoldGreen && green >= minGoldGreen) && (blue <= maxGoldBlue && blue >= minGoldBlue)) {
                    goldCenter += 1;
                }
            }
        }
        for (int i = 2 * (widthOfImage / 3); i < widthOfImage; i++) {
            for (int j = 0; j < heightOfImage; j++) {
                int pixel = picture.getPixel(i, j);
                int red = cameraController.red(pixel);
                int green = cameraController.green(pixel);
                int blue = cameraController.blue(pixel);
                if ((red <= maxGoldRed && red >= minGoldRed) && (green <= maxGoldGreen && green >= minGoldGreen) && (blue <= maxGoldBlue && blue >= minGoldBlue)) {
                    goldRight += 1;
                }
            }
        }
        if (goldRight > goldLeft) {
            if (goldRight > goldCenter) {
                return "right";
            } else {
                return "center";
            }
        } else {
            if (goldLeft > goldCenter) {
                return "left";
            } else {
                return "center";
            }
        }
    }
}
