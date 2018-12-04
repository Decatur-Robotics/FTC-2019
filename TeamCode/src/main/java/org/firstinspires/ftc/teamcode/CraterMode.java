package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Facing Crater", group="Autonomous")

public class CraterMode extends OpMode {

    Hardware_5177 robot = new Hardware_5177();
    private ElapsedTime runtime = new ElapsedTime();
    private TeamImu imu;

    public void reset(){
        robot.rightDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void init(){
        imu = new TeamImu().initialize(hardwareMap, telemetry);
    }
    public void init_loop(){}

    public void setPower(double rightPower, double leftPower){
        //robot.backDrive.setPower(power);
        robot.rightDrive.setPower(-rightPower);
        robot.leftDrive.setPower(-leftPower);
    }

    public void bothPower(double power){
        setPower(power, power);
    }

    public void fowardInches(double inches, double power){
        reset();
        if (inches > 0) {
            bothPower(power);
        }
        else {
            bothPower(-power);
        }
        while (true){
            if (Math.abs(robot.rightDrive.getCurrentPosition())/*/toInchesConstant*/ > inches && Math.abs(robot.leftDrive.getCurrentPosition())/*/toInchesConstant*/ > inches){
                bothPower(0);
                break;
            }
        }
    }

    public void turnRight(double degrees){
        setPower(-1, 1);
        while (true){
            if (/*robotDegrees >= degrees*/false){
                bothPower(0);
                return;
            }
        }
    }

    public void turnLeft(double degrees){
        setPower(1, -1);
        while (true){
            if (/*robotDegrees >= degrees*/false){
                bothPower(0);
                return;
            }
        }
    }

    @Override
    public void start () {
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        reset();
        robot.backDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        //telemetry.addData("Path0", "Starting at %7d :%7d", robot.leftMotor.getCurrentPosition(), robot.rightMotor.getCurrentPosition());
        //telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        bothPower(1);
        robot.arm.setPower(.1);
        while (true) {
            robot.rightClaw.setPosition(0);
            robot.leftClaw.setPosition(150);
            if (robot.rightDrive.getCurrentPosition() / 120 < -25) {
                bothPower(0);
                telemetry.addData("encoder position", robot.rightDrive.getCurrentPosition());
                telemetry.update();
                robot.rightClaw.setPosition(170);
                robot.leftClaw.setPosition(00);
                break;
            }
        }
    }
    @Override
    public void loop(){
    }
}
