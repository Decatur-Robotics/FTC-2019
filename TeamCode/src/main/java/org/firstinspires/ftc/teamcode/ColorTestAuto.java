package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;


@Autonomous(name="ColorTest", group="Autonomous")
public class ColorTestAuto extends OpMode {

    private AutomodeFunctionality automode = new AutomodeFunctionality();

    public void init(){};
    public void init_loop(){};

    @Override
    public void start () {
        //telemetry.addData("Status", automode.pictureAndCheck());
    }

    @Override
    public void loop () {
        //telemetry.addData("Status", automode.pictureAndCheck());
    }
}
