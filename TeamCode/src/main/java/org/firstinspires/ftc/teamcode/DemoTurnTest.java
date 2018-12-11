package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="TurnTest", group="Autonomous")
public class DemoTurnTest extends LinearOpMode {


    Hardware_Demo robot = new Hardware_Demo();
    private ElapsedTime period  = new ElapsedTime();

    //TODO: Calculate this experimentally
    static final double COUNTS_PER_DEGREE_TURNED = 14;
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
        double leftInches = (degrees * COUNTS_PER_DEGREE_TURNED) / COUNTS_PER_INCH;
        double rightInches = (degrees * COUNTS_PER_DEGREE_TURNED) / COUNTS_PER_INCH;
        double speedLeft = speed;
        double speedRight = speed;
        if (degrees < 0)
        {
            leftInches = leftInches;
            rightInches = -rightInches;
        }
        else
        {
            leftInches = -leftInches;
            rightInches = rightInches;
        }

        int timeoutS = 10;
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
        rotateDegrees(45, .7);
        moveInches(6, 6, .7, .7, 15);
    }
}