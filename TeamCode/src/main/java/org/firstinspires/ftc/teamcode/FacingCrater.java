package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//TODO: Test this entire opmode, it likely won't work great.

@Autonomous(name="FacingCrate", group="Autonomous")

public class FacingCrater extends LinearOpMode {
    Hardware_4232 robot = new Hardware_4232();
    private ElapsedTime runtime = new ElapsedTime();
    private AutomodeFunctionality automode = new AutomodeFunctionality();
    private boolean dropping = true;
    private boolean drivingToCrater = false;

    @Override
    public void runOpMode () {
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d", robot.leftMotor.getCurrentPosition(), robot.rightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //Code to drop
        if (dropping)
        {
            //TODO: Change the hardcoded number until you land and the hook is free, Also change the + sign to a - if it doesn't work or breaks.
            //Set motor target
            int motorTarget = robot.rack.getCurrentPosition() + 2830;
            robot.rack.setTargetPosition(motorTarget);
            //Run to position
            robot.rack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rack.setPower(0.5);
            while (robot.rack.isBusy())
            {
                telemetry.addData("Path0","Robot dropping\n");
                telemetry.update();
            }
            //Cut power
            robot.rack.setPower(0);
            //TODO: Change the numbers from positive to negative if the front is facing the lander
            automode.moveInches(24, 24, .7, 5, robot, false);
            dropping = false;
            drivingToCrater = true;
        }
        if (drivingToCrater)
        {
            //TODO: Uncomment this line if robot front facing lander
            //automode.rotateDegrees(180, robot);
            automode.moveInches(108, 108, 0.8, 15, robot, false);
            drivingToCrater = false;
        }
    }
}
