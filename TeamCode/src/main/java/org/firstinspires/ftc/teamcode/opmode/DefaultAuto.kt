package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.AutoBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import org.firstinspires.ftc.teamcode.subsystem.VisionSubsystem

@Autonomous(name="Default Auto")
class DefaultAuto: AutoBase() {
    // private lateinit var driveSubsystem: MecanumDriveSubsystem
    private lateinit var visionSubsystem: VisionSubsystem

    override fun onInit() {
        // driveSubsystem = BallDriveSubsystem(hardwareMap)
        visionSubsystem = VisionSubsystem(hardwareMap)

        // register(driveSubsystem)
        register(visionSubsystem)
    }

    override suspend fun onStart() {
        while (isRunning) {
            wait(0.1)

            visionSubsystem.doTelemetry(telemetry)
            telemetry.update()
        }
    }
}