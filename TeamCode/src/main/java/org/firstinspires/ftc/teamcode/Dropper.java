package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Dropper", group="Autonomous")

public class Dropper extends OpMode {
    Hardware_4232 robot = new Hardware_4232();

    public void init(){};
    public void init_loop(){};
    @Override
    public void start()
    {
        robot.init(hardwareMap);
        int motorTarget = robot.rack.getCurrentPosition() + 2830;
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
    }
    public void loop(){}
}
