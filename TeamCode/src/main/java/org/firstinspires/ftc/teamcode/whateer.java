package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;



import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class whateer {
    private Limelight3A limelight;
    private DcMotor motorBR;
    private DcMotor motorFR;
    private DcMotor motorFL;
    private DcMotor motorBL;
    private Servo launchServo;
    private Servo intakeLeft;
    private Servo intakeMiddle;
    private DcMotor shooterL;
    private DcMotor shooterR;
    private DcMotor intakeMotor;
    private Servo intakeRight;

    double frontLeftPower;
    double backLeftPower;
    double frontRightPower;
    double backRightPower;


}
