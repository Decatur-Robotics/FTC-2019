package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

//TODO: Test this entire opmode, it likely won't work great.

@Autonomous(name="Land", group="Autonomous")

public class LandAuto extends OpMode {
    Hardware_4232 robot = new Hardware_4232();
    private ElapsedTime runtime = new ElapsedTime();
    private AutomodeFunctionality automode = new AutomodeFunctionality();
    private boolean landed = false;
    private double startTime;
    private int state = 0;
    public void init(){robot.init(hardwareMap);}
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
        runtime.reset();
        startTime = runtime.seconds();

        // Wait for the game to start (driver presses PLAY)
    }
    @Override
    public void loop(){
        switch(state) {
            case 0:
                if (!landed && runtime.seconds() - startTime < 2.3) {
                robot.rack.setPower(1);
                } else {
                landed = true;
                robot.rack.setPower(0);
                }
                break;
        }
        telemetry.addData("Runtime", runtime.seconds());
    }
}
