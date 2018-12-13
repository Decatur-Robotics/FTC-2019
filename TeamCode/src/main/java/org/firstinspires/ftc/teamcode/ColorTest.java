package org.firstinspires.ftc.teamcode;

import android.graphics.Bitmap;
import android.hardware.Camera;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name="ColorTest", group="Autonomous")
public class ColorTest extends OpMode {
    private Camera camera;
    private CameraController cameraController;
    private Bitmap image;

    @Override
    public void init () {
        cameraController.startCamera();
        camera = cameraController.getCamera();
        telemetry.setAutoClear(false);
    }
    @Override
    public void start () {
        cameraController.stopCameraInSecs(35);
    }
    @Override
    public void loop () {
        if (cameraController.isImageReady())
        {
            image = cameraController.convertYuvImageToBitmap(cameraController.yuvImage);
            if (image != null)
            {
                telemetry.addData("Side is ", "%s", whichSide(image));
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
