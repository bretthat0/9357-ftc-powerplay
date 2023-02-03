package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.firstinspires.ftc.teamcode.ext.AprilTagDetectionPipeline
import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.openftc.apriltag.AprilTagDetection
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation


class VisionSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    private lateinit var camera: OpenCvCamera
    private lateinit var aprilTagDetectionPipeline: AprilTagDetectionPipeline

    val visibleTags: ArrayList<AprilTagDetection>
        get() = aprilTagDetectionPipeline.latestDetections

    override fun onRegister() {
        val cameraMonitorViewId = hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            hardwareMap.appContext.packageName
        )
        camera = OpenCvCameraFactory.getInstance().createWebcam(
            hardwareMap.get(WebcamName::class.java, CAM_NAME),
            cameraMonitorViewId
        )
        aprilTagDetectionPipeline = AprilTagDetectionPipeline(TAG_SIZE, FX, FY, CX, CY)

        camera.setPipeline(aprilTagDetectionPipeline)
        camera.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(CAM_WIDTH, CAM_HEIGHT, OpenCvCameraRotation.UPRIGHT)
            }

            override fun onError(errorCode: Int) {}
        })
    }

    override fun execute() {
        // Nothing ...
    }

    fun doTelemetry(telemetry: Telemetry) {
        for (tag in visibleTags) {
            telemetry.addLine("Visible tag: " + tag.id)
        }
    }

    companion object {
        // Lens intrinsics of Logitech C615
        // Focal point
        const val FX = 394.8387 // pixels
        const val FY = 394.8387 // pixels
        // Optical center
        const val CX = 960.000 // pixels
        const val CY = 540.000 // pixels

        const val CAM_NAME = "Webcam 1"
        const val CAM_WIDTH = 1920 // pixels
        const val CAM_HEIGHT = 1080 // pixels

        const val TAG_SIZE = 1.425 * (1.0 / 39.3701) // in * (m/in) = meters
    }
}