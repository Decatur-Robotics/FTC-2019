
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//TODO: Test this entire opmode, it likely won't work great.

@Autonomous(name="FacingCrate", group="Autonomous")

public class AutoMode extends OpMode {
    Hardware_5177 robot = new Hardware_5177();
    private ElapsedTime runtime = new ElapsedTime();
    private TeamImu imu;


    public void init(){
        imu = new TeamImu().initialize(hardwareMap, telemetry);
    }
    public void init_loop(){}


    public void foward(double power){
        //robot.backDrive.setPower(power);
        robot.rightDrive.setPower(power);
        robot.leftDrive.setPower(power);
    }

    public void delay(double delaySecs){
        for (int i = 0; i < delaySecs*1000; i++){}
    }
    @Override

    public void start () {
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        robot.backDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        //telemetry.addData("Path0", "Starting at %7d :%7d", robot.leftMotor.getCurrentPosition(), robot.rightMotor.getCurrentPosition());
        //telemetry.update();

        // Wait for the game to start (driver presses PLAY)
    }
    @Override
    public void loop(){
        foward(1);
        delay(1.5);
        foward(0);

    }
}
 