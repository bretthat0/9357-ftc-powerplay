package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.AutoBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import org.firstinspires.ftc.teamcode.subsystem.VisionSubsystem

@Autonomous(name="Default Auto")
class DefaultAuto: AutoBase() {
    private lateinit var driveSubsystem: MecanumDriveSubsystem
    private lateinit var armSubsystem: ArmSubsystem
    private lateinit var visionSubsystem: VisionSubsystem

    private val tagVisible: Boolean
        get() = visionSubsystem.visibleTags.isNotEmpty()

    enum class Direction {
        FORWARD, BACK, LEFT, RIGHT
    }

    override fun onInit() {
        driveSubsystem = MecanumDriveSubsystem(hardwareMap)
        armSubsystem = ArmSubsystem(hardwareMap)
        visionSubsystem = VisionSubsystem(hardwareMap)

        armSubsystem.mode = ArmSubsystem.Mode.Manual

        register(driveSubsystem)
        register(visionSubsystem)
    }

    override suspend fun onStart() {
        while (isRunning) {
            // Capture ID of visible tag
            val tagId = if (tagVisible) visionSubsystem.visibleTags[0].id else 0

            wait(1.0)

            // INSTRUCTIONS
            claw(true)
            drive(Direction.FORWARD, 0.5)
            positionArm(90, 0)
            claw(false)
            positionArm(0, 0)
            drive(Direction.BACK, 0.5)

            // Do different instructions depending on what tag was read at start
            // e.g. park in different spots
            when (tagId) {
                0 -> {
                    // ...
                }
                1 -> {
                    // ...
                }
                2 -> {
                    // ...
                }
            }
        }
    }

    private suspend fun drive(direction: Direction, seconds: Double) {
        driveSubsystem.leftInput = vec2(direction)
        wait(seconds)
    }

    private suspend fun claw(grabbing: Boolean) {
        armSubsystem.isGrabbing = grabbing
        wait(0.5)
    }

    private suspend fun positionArm(pivotDegrees: Int, extendDegrees: Int) {
        armSubsystem.pivotPosition = pivotDegrees / 360.0
        armSubsystem.extendPosition = extendDegrees / 360.0
        wait(2.0)
    }

    private suspend fun positionTurret(rotateDegrees: Int) {
        armSubsystem.rotatePosition = rotateDegrees / 360.0
        wait(2.0)
    }

    private fun vec2(dir: Direction): Vector2 {
        return when (dir) {
            Direction.FORWARD -> vec2(0.0, 1.0)
            Direction.BACK -> vec2(0.0, -1.0)
            Direction.LEFT -> vec2(-1.0, 0.0)
            Direction.RIGHT -> vec2(1.0, 0.0)
        }
    }
}