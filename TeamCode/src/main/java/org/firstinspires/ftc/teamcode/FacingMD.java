package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware_4232;

//TODO: Test this entire opmode, it likely won't work great.

@Autonomous(name="FacingMD", group="Autonomous")

public class FacingMD extends OpMode {
    Hardware_4232 robot = new Hardware_4232();
    private ElapsedTime runtime = new ElapsedTime();
    private boolean dropping = true;
    private boolean drivingToCrater = false;

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

    @Override
    public void init(){
        robot.init(hardwareMap);
        telemetry.addData("line 21", "line21");
        telemetry.update();
    }
    public void init_loop(){}


    @Override
    public void start () {

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0", "Starting at %7d :%7d", robot.leftMotor.getCurrentPosition(), robot.rightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
    }
    @Override
    public void loop(){
        //Code to drop
        if (dropping)
        {
            //TODO: Change the hardcoded number until you land and the hook is free, Also change the + sign to a - if it doesn't work or breaks.
            //Set motor target
            int motorTarget = robot.rack.getCurrentPosition() + 3250;
            robot.rack.setTargetPosition(motorTarget);
            //Run to position
            robot.rack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rack.setPower(-1);
            while (robot.rack.isBusy())
            {
                telemetry.addData("Path0","Robot dropping\n");
                telemetry.update();
            }
            //Cut power
            robot.rack.setPower(0);
            moveInches(-4, -4, .7, 5, robot, false);
            //automode.rotateDegrees(90, robot);
            dropping = false;
            drivingToCrater = true;
            motorTarget = robot.rack.getCurrentPosition() + 3500;
            robot.rack.setTargetPosition(motorTarget);
            robot.rack.setPower(1);
            while (robot.rack.isBusy())
            {
                telemetry.addData("Path0","Rack dropping\n");
                telemetry.update();
            }
            //Cut power
            robot.rack.setPower(0);
        }
        if (drivingToCrater)
        {
            moveInches(-82, -82, .7, 15, robot, false);
            //TODO: Add Mascot dropping code here
            robot.mascot_dropper.setPosition(180);
           // automode.rotateDegrees(-50, robot);
            moveInches(132, 132, .8, 15, robot, false);
            drivingToCrater = false;
        }
    }
}
