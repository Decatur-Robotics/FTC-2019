package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//TODO: Test this entire opmode, it likely won't work great.

@Autonomous(name="FacingCrater", group="Autonomous")

public class FacingCrater extends LinearOpMode {
    Hardware_4232 robot = new Hardware_4232();
    private ElapsedTime runtime = new ElapsedTime();
    private boolean dropping = true;
    private boolean drivingToCrater = false;

    private ElapsedTime period  = new ElapsedTime();

    //TODO: Calculate this experimentally
    static final double COUNTS_PER_DEGREE_TURNED = 10;
    static final double COUNTS_TO_DROP = 5600;
    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double     WIDTH                   = 18;
    static final double     CIRCUMFERENCE           = (WIDTH * 3.1415);

    public void rotateDegrees(double degrees, double speed)
    {
        double leftDistance = (degrees * COUNTS_PER_DEGREE_TURNED) / COUNTS_PER_INCH;
        double rightDistance = (degrees * COUNTS_PER_DEGREE_TURNED) / COUNTS_PER_INCH;
        double leftSpeed;
        double rightSpeed;
        if (degrees < 0)
        {
            leftSpeed = -speed;
            rightSpeed = speed;
        }
        else
        {
            leftSpeed = speed;
            rightSpeed = -speed;
        }
        moveInches(rightDistance, leftDistance, rightSpeed, leftSpeed, 10);
    }

    public void moveInches(double rightInches, double leftInches, double speedRight, double speedLeft, int timeoutS) {
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
        robot.leftMotor.setPower(Range.clip(speedLeft, 1.0, -1.0));
        robot.rightMotor.setPower(Range.clip(speedRight, 1.0, -1.0));
        //Loop until done or at position
        while (opModeIsActive() && (period.seconds() < timeoutS) && (robot.leftMotor.isBusy() || robot.rightMotor.isBusy()))
        {}
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        robot.rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        while (!(isStarted() || isStopRequested())) {

            // Display the light level while we are waiting to start
            idle();
        }
        int motorTarget = robot.rack.getCurrentPosition() + 5600;
        robot.rack.setTargetPosition(motorTarget);
        //Run to position
        robot.rack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rack.setPower(-1);
        while (opModeIsActive() && robot.rack.isBusy())
        {
            telemetry.addData("Path0","Robot dropping\n");
            telemetry.update();
        }
        //Cut power
        robot.rack.setPower(0);
        int leftTarget;
        int rightTarget;
        //Calculate Target
        leftTarget = robot.leftMotor.getCurrentPosition() + 1440;
        rightTarget = robot.rightMotor.getCurrentPosition() + 1440;
        //Set targets to motors
        robot.leftMotor.setTargetPosition(leftTarget);
        robot.rightMotor.setTargetPosition(rightTarget);
        //Set motors to run to position
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //Reset time and run motion
        robot.leftMotor.setPower(Range.clip(-.7, 1.0, -1.0));
        robot.rightMotor.setPower(Range.clip(-.7, 1.0, -1.0));
        //Loop until done or at position
        while (opModeIsActive() && (robot.leftMotor.isBusy() || robot.rightMotor.isBusy())) {
        }
        robot.leftMotor.setPower(0);
        robot.rightMotor.setPower(0);

        // Turn off RUN_TO_POSITION
        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robot could be facing to the side depending on where we put the rack - Scott
        //Well it isn't so ha - Keon
        moveInches(1, 1, .7, .7, 7);
        rotateDegrees(-45, .5);
        moveInches(40, 40, .7, .7, 15);
        /*motorTarget = robot.rack.getCurrentPosition() + (int)COUNTS_TO_DROP;
        robot.rack.setTargetPosition(motorTarget);
        robot.rack.setPower(1);
        while (opModeIsActive() && robot.rack.isBusy())
        {
            telemetry.addData("Path0","Rack dropping\n");
            telemetry.update();
        }
        //Cut power
        robot.rack.setPower(0);

        //TODO: Comment this line and uncomment lower parts to try dropping mascot
        rotateDegrees(-95, .5);
        moveInches(85, 85, 0.8, 0.8, 15);
        //rotateDegrees(-95, .5);
        //moveInches(24, 24, .8, .8, 15);
        //rotateDegrees(107.693, .7);
        //moveInches(110, 110, -.8, -.8, 15);
        //robot.mascot_dropper.setPosition(1);
        //rotateDegrees(-62.69, .7);
        //moveInches(108, 108, .8, .8, 15);
        drivingToCrater = false;*/
    }



}
