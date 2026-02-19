package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "whateer5 (Blocks to Java)")
public class whateer extends LinearOpMode {

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



    @Override
    public void runOpMode() {
        ElapsedTime runtime;
        float triggerValue;
        float axial;
        float lateral;
        float yaw;
        double max;
        boolean doAutoAim = false ;
        int servoBTimer = 0;
        int servoATimer = 0;
        int servoYTimer = 0;
        int servoXTimer = 0;
        LLResult result = null;

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        motorBR = hardwareMap.get(DcMotor.class, "motorBR");
        motorFR = hardwareMap.get(DcMotor.class, "motorFR");
        motorFL = hardwareMap.get(DcMotor.class, "motorFL");
        motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        launchServo = hardwareMap.get(Servo.class, "launchServo");
        intakeLeft = hardwareMap.get(Servo.class, "intakeLeft");
        intakeMiddle = hardwareMap.get(Servo.class, "intakeMiddle");
        shooterL = hardwareMap.get(DcMotor.class, "shooterL");
        shooterR = hardwareMap.get(DcMotor.class, "shooterR");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        intakeRight = hardwareMap.get(Servo.class, "intakeRight");

        limelight.pipelineSwitch(0);
        limelight.start();
        runtime = new ElapsedTime();
        // ########################################################################################
        // !!! IMPORTANT Drive Information. Test your motor directions. !!!!!
        // ########################################################################################
        //
        // Most robots need the motors on one side to be reversed to drive forward.
        // The motor reversals shown here are for a "direct drive" robot
        // (the wheels turn the same direction as the motor shaft).
        //
        // If your robot has additional gear reductions or uses a right-angled drive, it's important to ensure
        // that your motors are turning in the correct direction. So, start out with the reversals here, BUT
        // when you first test your robot, push the left joystick forward and observe the direction the wheels turn.
        //
        // Reverse the direction (flip FORWARD <-> REVERSE ) of any wheel that runs backward.
        // Keep testing until ALL the wheels move the robot forward when you push the left joystick forward.
        // <--- Click blue icon to see important note re. testing motor directions.
        motorBR.setDirection(DcMotor.Direction.FORWARD);
        motorFR.setDirection(DcMotor.Direction.REVERSE);
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        motorBL.setDirection(DcMotor.Direction.FORWARD);
        launchServo.setDirection(Servo.Direction.REVERSE);
        // Wait for the game to start (driver presses START)
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        runtime.reset();
        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            // Note: pushing stick forward gives negative value
            triggerValue = gamepad2.left_trigger;
            axial = gamepad1.left_stick_x;
            lateral = -gamepad1.left_stick_y;
            yaw = gamepad1.right_stick_x;
            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            frontLeftPower = axial + lateral;
            frontRightPower = axial - lateral;
            backLeftPower = axial - lateral;
            backRightPower = axial + lateral;
            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = JavaUtil.maxOfList(JavaUtil.createListWith(Math.abs(frontLeftPower), Math.abs(frontRightPower), Math.abs(backLeftPower), Math.abs(backRightPower)));
            if (max > 1) {
                frontLeftPower = frontLeftPower / max;
                frontRightPower = frontRightPower / max;
                backLeftPower = backLeftPower / max;
                backRightPower = backRightPower / max;
            }
            // Send calculated power to wheels.
            if (!doAutoAim) {
                motorFL.setPower(frontLeftPower);
                motorFR.setPower(frontRightPower);
                motorBL.setPower(backLeftPower);
                motorBR.setPower(backRightPower);
            }
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime);
            telemetry.addData("Front left/Right", JavaUtil.formatNumber(frontLeftPower, 4, 2) + ", " + JavaUtil.formatNumber(frontRightPower, 4, 2));
            telemetry.addData("Back  left/Right", JavaUtil.formatNumber(backLeftPower, 4, 2) + ", " + JavaUtil.formatNumber(backRightPower, 4, 2));
            telemetry.addData("gamepad yaw", yaw);
            telemetry.addData("timer B", servoBTimer);
            telemetry.addData("timer A", servoATimer);
            telemetry.addData("timer Y", servoYTimer);
            telemetry.addData("timer X", servoXTimer);
            telemetry.addData("do auto aim", doAutoAim);
            telemetry.update();
            intakeLeft.setDirection(Servo.Direction.REVERSE);
            intakeMiddle.setDirection(Servo.Direction.REVERSE);
            if (triggerValue > 0) {
                shooterL.setPower(-0.6);
                shooterR.setPower(0.6);
            } else {
                shooterR.setPower(0);
                shooterL.setPower(0);
            }
            if (gamepad2.y) {
                servoYTimer = 15;
            }
            if (gamepad2.x) {
                servoXTimer = 10;
            }
            if (gamepad2.a) {
                servoATimer = 10;
            }
            if (gamepad2.b) {
                servoBTimer = 10;
            }
            if (gamepad2.right_bumper) {
                intakeMotor.setPower(1);
            } else if (gamepad2.left_bumper) {
                intakeMotor.setPower(-1);
            } else {
                intakeMotor.setPower(0);
            }
            if (servoATimer > 0) {
                intakeMiddle.setPosition(0.3);
            } else {
                intakeMiddle.setPosition(0);
            }
            if (servoBTimer > 0) {
                intakeLeft.setPosition(0.3);
            } else {
                intakeLeft.setPosition(0);
            }
            if (servoXTimer > 0) {
                intakeRight.setPosition(0.5);
            } else {
                intakeRight.setPosition(0);
            }
            if (servoYTimer > 0) {
                launchServo.setPosition(0.5);
            } else {
                launchServo.setPosition(0);
            }
            if (servoYTimer >= 0) {
                servoYTimer -= 1;
            }
            if (servoXTimer >= 0) {
                servoXTimer -= 1;
            }
            if (servoATimer >= 0) {
                servoATimer -= 1;
            }
            if (servoBTimer >= 0) {
                servoBTimer -= 1;
            }
            if (gamepad1.dpad_up) {
                doAutoAim = true;
            }
            if (gamepad1.dpad_down) {
                doAutoAim = false;
            }
            if (doAutoAim) {
                if (result.getTx() < 15) {
                    motorBL.setPower(0.3);
                    motorFL.setPower(0.3);
                    motorBR.setPower(-0.3);
                    motorFR.setPower(-0.3);
                } else if (result.getTx() > -15) {
                    motorFL.setPower(-0.3);
                    motorBL.setPower(-0.3);
                    motorFR.setPower(0.3);
                    motorBR.setPower(0.3);
                } else if (result.getTx() >= -10 && result.getTx() < -5) {
                    motorBR.setPower(0.1);
                    motorFR.setPower(0.1);
                    motorFL.setPower(-0.1);
                    motorBL.setPower(-0.1);
                } else if (result.getTx() <= 10 && result.getTx() > 5) {
                    motorFL.setPower(0.1);
                    motorBL.setPower(0.1);
                    motorBR.setPower(-0.1);
                    motorFR.setPower(-0.1);
                } else {
                    intakeMotor.setPower(0);
                    motorBL.setPower(0);
                    intakeMotor.setPower(0);
                    motorBL.setPower(0);
                }
            }
            if (!doAutoAim) {
                motorFL.setPower(yaw);
                motorBL.setPower(-1 * yaw);
                motorBR.setPower(-1 * yaw);
                motorFR.setPower(yaw);
            }
        }
    }



}