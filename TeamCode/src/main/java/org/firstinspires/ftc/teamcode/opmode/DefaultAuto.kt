package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.AutoBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import org.firstinspires.ftc.teamcode.subsystem.VisionSubsystem

@Autonomous(name="Default Auto")
open class DefaultAuto: AutoBase() {
    private lateinit var driveSubsystem: MecanumDriveSubsystem
    private lateinit var armSubsystem: ArmSubsystem
    private lateinit var visionSubsystem: VisionSubsystem

    protected var tagId = 1
    protected val tagVisible: Boolean
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
            wait(1.0)

            // Capture ID of visible tag
            if (tagVisible) {
                tagId = visionSubsystem.visibleTags[0].id
            }

            executeInstructions()

            requestOpModeStop()
        }
    }

    protected open suspend fun executeInstructions() {
        when (tagId) {
            0 -> drive(Direction.LEFT, 1.0)
            2 -> drive(Direction.RIGHT, 1.0)
        }

        drive(Direction.FORWARD, 0.5)
    }

    protected suspend fun drive(direction: Direction, seconds: Double) {
        driveSubsystem.leftInput = vec2(direction)
        driveSubsystem.execute()
        wait(seconds)
    }

    protected suspend fun claw(grabbing: Boolean) {
        armSubsystem.isGrabbing = grabbing
        wait(0.5)
    }

    protected suspend fun positionArm(pivotDegrees: Int, extendDegrees: Int) {
        armSubsystem.pivotPosition = pivotDegrees / 360.0
        armSubsystem.extendPosition = extendDegrees / 360.0
        wait(2.0)
    }

    protected suspend fun positionTurret(rotateDegrees: Int) {
        armSubsystem.rotatePosition = rotateDegrees / 360.0
        wait(2.0)
    }

    protected fun vec2(dir: Direction): Vector2 {
        return when (dir) {
            Direction.FORWARD -> vec2(0.0, 1.0)
            Direction.BACK -> vec2(0.0, -1.0)
            Direction.LEFT -> vec2(-1.0, 0.0)
            Direction.RIGHT -> vec2(1.0, 0.0)
        }
    }
}