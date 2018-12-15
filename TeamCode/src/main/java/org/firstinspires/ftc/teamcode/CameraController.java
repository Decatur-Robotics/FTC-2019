package org.firstinspires.ftc.teamcode;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class CameraController extends Application{
    public Camera camera;
    public CameraPreview preview;

    public int width;
    public int height;
    public YuvImage yuvImage = null;

    volatile private boolean imageReady = false;

    private int looped = 0;
    private String data;
    private int ds = 1; // downsampling parameter

    public CameraController() {}

    public Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                width = parameters.getPreviewSize().width;
                height = parameters.getPreviewSize().height;
                yuvImage = new YuvImage(data, ImageFormat.NV21, width, height, null);
                imageReady = true;
                looped++;
            } catch (Exception e) {}
        }
    };

    public void setCameraDownsampling(int downSampling) {
        ds = downSampling;
    }

    public boolean isImageReady() {
        return imageReady;
    }

    public boolean isCameraAvailable() {
        int cameraId = -1;
        Camera cam = null;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) { // Camera.CameraInfo.CAMERA_FACING_FRONT or BACK
                cameraId = i;
                break;
            }
        }
        try {
            cam = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e("Error", "Camera Not Available!");
            return false;
        }
        cam.release();
        cam = null;
        return true;
    }

    public Camera openCamera(int cameraInfoType) {
        int cameraId = -1;
        Camera cam = null;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == cameraInfoType) { // Camera.CameraInfo.CAMERA_FACING_FRONT or BACK
                cameraId = i;
                break;
            }
        }
        try {
            cam = Camera.open(cameraId);
        } catch (Exception e) {
            Log.e("Error", "Can't Open Camera");
        }
        return cam;
    }

    public Camera startCamera() {

        camera = openCamera(Camera.CameraInfo.CAMERA_FACING_BACK);
        camera.setPreviewCallback(previewCallback);

        Camera.Parameters parameters = camera.getParameters();
        width = parameters.getPreviewSize().width / ds;
        height = parameters.getPreviewSize().height / ds;
        parameters.setPreviewSize(width, height);

        camera.setParameters(parameters);
        //preview = new CameraPreview(getApplicationContext(), camera, previewCallback);

        data = parameters.flatten();
/*
        if(preview == null) {
            ((FtcRobotControllerActivity) hardwareMap.appContext).initPreviewLinear(camera, this, previewCallback);
        }
*/      return camera;
    }

    public void stopCameraInSecs(int duration) {
        Thread cameraKillThread = new Thread(new CameraKillThread(duration));

        cameraKillThread.start();
    }

    public class CameraKillThread implements Runnable {
        int dur;

        public CameraKillThread(int duration) {
            dur = duration;
        }

        public void run() {
            try {
                Thread.sleep(dur * 1000, 0);
            } catch (InterruptedException ex) {

            }

            stopCamera();
            imageReady = false;
        }
    }

    public void stopCamera() {
        if (camera != null) {
            if (preview != null) {
                //((FtcRobotControllerActivity) hardwareMap.appContext).removePreviewLinear(this);
                preview = null;
            }
            camera.stopPreview();
            camera.setPreviewCallback(null);
            if (camera != null) {
                camera.release();
            }
            camera = null;
        }
    }

    static public int red(int pixel) {
        return (pixel >> 16) & 0xff;
    }

    static public int green(int pixel) {
        return (pixel >> 8) & 0xff;
    }

    static public int blue(int pixel) {
        return pixel & 0xff;
    }

    static public int gray(int pixel) {
        return (red(pixel) + green(pixel) + blue(pixel));
    }

    static public int highestColor(int red, int green, int blue) {
        int[] color = {red, green, blue};
        int value = 0;
        for (int i = 1; i < 3; i++) {
            if (color[value] < color[i]) {
                value = i;
            }
        }
        return value;
    }

    static public Bitmap convertYuvImageToBitmap(YuvImage yuvImage) {
        byte[] bits = yuvImage.getYuvData();
        Bitmap bitmap = null;
        bitmap = Bitmap.createBitmap(yuvImage.getWidth(), yuvImage.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bits));
        return bitmap;
    }

    public Camera getCamera()
    {
        return camera;
    }
}