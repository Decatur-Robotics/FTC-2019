package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="Dropper", group="Autonomous")

public class Dropper extends LinearOpMode {
    Hardware_4232 robot = new Hardware_4232();
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        robot.leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        robot.rightMotor.setDirection(DcMotor.Direction.FORWARD);// Set to FORWARD if using AndyMark motors
        while (!(isStarted() || isStopRequested())) {

            // Display the light level while we are waiting to start
            idle();
        }
        int motorTarget = robot.rack.getCurrentPosition() + 10272;
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


    }

}
