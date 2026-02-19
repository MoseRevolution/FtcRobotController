package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.JavaUtil;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

@Autonomous(name = "letMeCook2 (Blocks to Java)")
public class letMeCook extends LinearOpMode {

    private DcMotor motorBL;
    private DcMotor motorFL;
    private Limelight3A limelight;
    private DcMotor motorBR;
    private DcMotor motorFR;

    /**
     * This OpMode illustrates how to use the Limelight3A Vision Sensor.
     *
     * See https://limelightvision.io/
     *
     * Notes on configuration:
     *
     * The device presents itself, when plugged into a USB port on a Control Hub as
     * an ethernet interface. A DHCP server running on the Limelight automatically
     * assigns the Control Hub an ip address for the new ethernet interface.
     *
     * Since the Limelight is plugged into a USB port, it will be listed on the
     * top level configuration activity along with the Control Hub Portal and other
     * USB devices such as webcams. Typically serial numbers are displayed below
     * the device's names. In the case of the Limelight device, the Control Hub's
     * assigned ip address for that ethernet interface is used as the "serial number".
     *
     * Tapping the Limelight's name, transitions to a new screen where the user can
     * rename the Limelight and specify the Limelight's ip address. Users should take care
     * not to confuse the ip address of the Limelight itself, which can be configured
     * through the Limelight settings page via a web browser, and the ip address
     * the Limelight device assigned the Control Hub and which is displayed in small
     * text below the name of the Limelight on the top level configuration screen.--
     */
    @Override
    public void runOpMode() {
        LLStatus status;
        LLResult result;
        Pose3D botpose;
        double captureLatency;
        double targetingLatency;
        List<LLResultTypes.FiducialResult> fiducialResults;
        LLResultTypes.FiducialResult fiducialResult;
        List<LLResultTypes.ColorResult> colorResults;
        LLResultTypes.ColorResult colorResult;

        motorBL = hardwareMap.get(DcMotor.class, "motorBL");
        motorFL = hardwareMap.get(DcMotor.class, "motorFL");
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        motorBR = hardwareMap.get(DcMotor.class, "motorBR");
        motorFR = hardwareMap.get(DcMotor.class, "motorFR");

        motorBL.setDirection(DcMotor.Direction.REVERSE);
        motorFL.setDirection(DcMotor.Direction.REVERSE);
        telemetry.setMsTransmissionInterval(11);
        limelight.pipelineSwitch(0);
        // Starts polling for data.  If you neglect to call start(), getLatestResult() will return null.
        limelight.start();
        telemetry.addData(">", "Robot Ready.  Press Play.");
        telemetry.update();
        waitForStart();
        while (opModeIsActive()) {
            status = limelight.getStatus();
            telemetry.addData("Name", status.getName());
            telemetry.addData("LL", "Temp: " + JavaUtil.formatNumber(status.getTemp(), 1) + "C, CPU: " + JavaUtil.formatNumber(status.getCpu(), 1) + "%, FPS: " + Math.round(status.getFps()));
            telemetry.addData("Pipeline", "Index: " + status.getPipelineIndex() + ", Type: " + status.getPipelineType());
            result = limelight.getLatestResult();
            if (result != null) {
                // Access general information.
                botpose = result.getBotpose();
                captureLatency = result.getCaptureLatency();
                targetingLatency = result.getTargetingLatency();
                telemetry.addData("PythonOutput", JavaUtil.makeTextFromList(result.getPythonOutput(), ","));
                telemetry.addData("tx", result.getTx());
                telemetry.addData("txnc", result.getTxNC());
                telemetry.addData("ty", result.getTy());
                telemetry.addData("tync", result.getTyNC());
                telemetry.addData("Botpose", botpose.toString());
                telemetry.addData("LL Latency", captureLatency + targetingLatency);
                // Access fiducial results.
                fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fiducialResult_item : fiducialResults) {
                    fiducialResult = fiducialResult_item;
                    telemetry.addData("Fiducial", "ID: " + fiducialResult.getFiducialId() + ", Family: " + fiducialResult.getFamily() + ", X: " + JavaUtil.formatNumber(fiducialResult.getTargetXDegrees(), 2) + ", Y: " + JavaUtil.formatNumber(fiducialResult.getTargetYDegrees(), 2));
                    // Access color results.
                    colorResults = result.getColorResults();
                    for (LLResultTypes.ColorResult colorResult_item : colorResults) {
                        colorResult = colorResult_item;
                        telemetry.addData("Color", "X: " + JavaUtil.formatNumber(colorResult.getTargetXDegrees(), 2) + ", Y: " + JavaUtil.formatNumber(colorResult.getTargetYDegrees(), 2));
                    }
                }
            } else {
                telemetry.addData("Limelight", "No data available");
            }
            telemetry.addLine("text");
            telemetry.update();
            limelight.updateRobotOrientation(45);
            if (result.getTx() > 15) {
                motorFL.setPower(0.3);
                motorBL.setPower(0.5);
                motorBR.setPower(-0.3);
                motorFR.setPower(-0.3);
            } else if (result.getTx() < -15) {
                motorFL.setPower(-0.3);
                motorBL.setPower(-0.3);
                motorBR.setPower(0.3);
                motorFR.setPower(0.3);
            } else if (result.getTx() >= -10 && result.getTx() < -5) {
                motorBR.setPower(0.1);
                motorFR.setPower(0.1);
                motorBL.setPower(-0.1);
                motorFL.setPower(-0.1);
            } else if (result.getTx() <= 10 && result.getTx() > 5) {
                motorBL.setPower(0.1);
                motorFL.setPower(0.1);
                motorBR.setPower(-0.1);
                motorFR.setPower(-0.1);
            } else {
                motorFL.setPower(0);
                motorBL.setPower(0);
                motorBR.setPower(0);
                motorFR.setPower(0);
            }
        }
        limelight.stop();
    }
}