package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//TODO: Test this entire opmode, it likely won't work great.

@Autonomous(name="FacingCrater", group="Autonomous")

public class FacingCrater extends OpMode {
    Hardware_4232 robot = new Hardware_4232();
    private ElapsedTime runtime = new ElapsedTime();
    private AutomodeFunctionality automode = new AutomodeFunctionality();
    private boolean dropping = true;
    private boolean drivingToCrater = false;


    public void init(){};
    public void init_loop(){};



    @Override
    public void start () {
        robot.init(hardwareMap);

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
            //robot could be facing to the side depending on where we put the rack - Scott
            //Well it isn't so ha - Keon
            automode.moveInches(-6, -6, .7, 5, robot, false);
            dropping = false;
            drivingToCrater = true;
            motorTarget = robot.rack.getCurrentPosition() + 2830;
            robot.rack.setTargetPosition(motorTarget);
            robot.rack.setPower(0.5);
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
            //TODO: Uncomment this line if robot front facing lander
            automode.rotateDegrees(118.58, robot);
            automode.moveInches(8.846 * 12, 8.846 * 12, 0.8, 15, robot, false);
            //TODO: Drop mascot here

            automode.rotateDegrees(151.42, robot);
            automode.moveInches(132, 132, 0.8, 15, robot, false);
            drivingToCrater = false;
        }
    }
}
