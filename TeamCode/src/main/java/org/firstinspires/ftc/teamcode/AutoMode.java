
package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.robot.Robot;
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


    public void setPower(int motorSide, double power){
        if (motorSide == 1) {
            robot.frontRight.setPower(power);
            robot.backRight.setPower(power);
        } else if (motorSide == 0){
            robot.frontLeft.setPower(power);
            robot.backLeft.setPower(power);
        } else if (motorSide == 2){
            setPower(1, power);
            setPower(0, power);
        }
    }

    public void turn(double degrees, double power){
        double destination = imu.getTotalDegreesTurned() + degrees;
        boolean straight = false;
        boolean right = false;
        if (imu.getTotalDegreesTurned() == destination){
            straight = true;
        }
        else {
            right = imu.getTotalDegreesTurned() > destination;
        }

        if (straight){
        } else if (right){
                setPower(1, power);
                setPower(0, -power);
            } else {
                setPower(1, -power);
                setPower(0, power);
        }

        while (true){
            if (right){
                if(imu.getTotalDegreesTurned() < destination){
                    setPower(2, 0);
                }
                else {
                    if(imu.getTotalDegreesTurned() > destination){
                        setPower(2, 0);
                    }
                }
            }
        }
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

        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        //telemetry.addData("Path0", "Starting at %7d :%7d", robot.leftMotor.getCurrentPosition(), robot.rightMotor.getCurrentPosition());
        //telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        setPower(2,-1);
        while (true) {
            //robot.rightClaw.setPosition(0);
            //robot.leftClaw.setPosition(150);
            if (robot.frontRight.getCurrentPosition() > 10000) {
                setPower(2,0);
                telemetry.addData("encoder position", robot.frontRight.getCurrentPosition());
                telemetry.update();
                //robot.rightClaw.setPosition(170);
                //robot.leftClaw.setPosition(00);
                break;
            }
        }
    }
    @Override
    public void loop(){
    }
}
 