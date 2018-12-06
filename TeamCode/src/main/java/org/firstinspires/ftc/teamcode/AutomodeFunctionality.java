package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.Hardware_4232;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import android.provider.MediaStore;
//TODO: Check this whole thing.

public class AutomodeFunctionality extends Activity{

    private ElapsedTime period  = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double     WIDTH                   = 18;
    static final double     CIRCUMFERENCE           = (WIDTH * 3.1415);

    private Bitmap imageBitmap;

    public AutomodeFunctionality(){

    }

    public void rotateDegrees(double degrees, Hardware_4232 robot) {
        if (degrees == 0)
        {
            return;
        }
        //Turn right if direction is positive, left if negative.
        //robot.gyro.calibrate();
        double destinationDegree;
        if (degrees <= 0)
        {
            destinationDegree = 360 + degrees;
        }
        else
        {
            destinationDegree = 0 + degrees;
        }
        /*while (robot.gyro.getHeading() <= (destinationDegree - 1) || robot.gyro.getHeading() >= (destinationDegree + 1))
        {
            //TODO: Check the .15 number. Reduce it if the robot never stops turning, increase if the robot takes too long to turn.
            if (destinationDegree <= 180)
            {
                moveInches(-0.15, 0.15, .5, 1, robot, true);
            }
            else
            {
                moveInches(0.15, -0.15, .5, 1, robot, true);
            }
        }*/
    }

    public void moveInches(double rightInches, double leftInches, double speed, int timeoutS, Hardware_4232 robot, boolean rotating) {
        int leftTarget;
        int rightTarget;
        //Calculate Target
        leftTarget = robot.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
        rightTarget = robot.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
        //Set targets to motors
        robot.leftMotor.setTargetPosition(leftTarget);
        robot.rightMotor.setTargetPosition(rightTarget);
        //Set motors to run to position
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //Reset time and run motion
        period.reset();
        robot.leftMotor.setPower(Range.clip(speed, 1.0, -1.0));
        robot.rightMotor.setPower(Range.clip(speed, 1.0, -1.0));
        //Loop until done or at position
        while ((period.seconds() < timeoutS) && (robot.leftMotor.isBusy() || robot.rightMotor.isBusy()))
        {}
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public String pictureAndCheck()
    {
        Camera cam = Camera.open();
        try {

            Parameters p = cam.getParameters();
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
        }
        catch (Exception e){
            System.out.println(e);
        }
        dispatchTakePictureIntent();
        int goldRight = 0;
        int goldLeft = 0;
        int goldCenter = 0;
        int heightOfImage = imageBitmap.getHeight();
        int widthOfImage = imageBitmap.getWidth();
        int maxGoldRed = 255;
        int minGoldRed = 200;
        int maxGoldGreen = 255;
        int minGoldGreen = 200;
        int maxGoldBlue = 142;
        int minGoldBlue = 88;
        for (int i = 0; i < widthOfImage / 3; i++)
        {
            for (int j = 0; j < heightOfImage; j++)
            {
                int pixel = imageBitmap.getPixel(i, j);
                int redMask = 0x00FF0000;
                int greenMask = 0x0000FF00;
                int blueMask = 0x000000FF;
                int red = (pixel & redMask) >> 8;
                int green = (pixel & greenMask) >> 4;
                int blue = (pixel & blueMask);
                if ((red <= maxGoldRed && red >= minGoldRed) && (green <= maxGoldGreen && green >= minGoldGreen) && (blue <= maxGoldBlue && blue >= minGoldBlue))
                {
                    goldLeft += 1;
                }
            }
        }
        for (int i = widthOfImage / 3; i < 2 * (widthOfImage / 3); i++)
        {
            for (int j = 0; j < heightOfImage; j++)
            {
                int pixel = imageBitmap.getPixel(i, j);
                int redMask = 0x00FF0000;
                int greenMask = 0x0000FF00;
                int blueMask = 0x000000FF;
                int red = (pixel & redMask) >> 8;
                int green = (pixel & greenMask) >> 4;
                int blue = (pixel & blueMask);
                if ((red <= maxGoldRed && red >= minGoldRed) && (green <= maxGoldGreen && green >= minGoldGreen) && (blue <= maxGoldBlue && blue >= minGoldBlue))
                {
                    goldCenter += 1;
                }
            }
        }
        for (int i = 2 * (widthOfImage / 3); i < widthOfImage; i++)
        {
            for (int j = 0; j < heightOfImage; j++)
            {
                int pixel = imageBitmap.getPixel(i, j);
                int redMask = 0x00FF0000;
                int greenMask = 0x0000FF00;
                int blueMask = 0x000000FF;
                int red = (pixel & redMask) >> 8;
                int green = (pixel & greenMask) >> 4;
                int blue = (pixel & blueMask);
                if ((red <= maxGoldRed && red >= minGoldRed) && (green <= maxGoldGreen && green >= minGoldGreen) && (blue <= maxGoldBlue && blue >= minGoldBlue))
                {
                    goldRight += 1;
                }
            }
        }
        try
        {
            cam.stopPreview();
            cam.release();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        if (goldRight > goldLeft)
        {
            if (goldRight > goldCenter)
            {
                return "right";
            }
            else
            {
                return "center";
            }
        }
        else
        {
            if (goldLeft > goldCenter)
            {
                return "left";
            }
            else
            {
                return "center";
            }
        }
    }
    */
}
