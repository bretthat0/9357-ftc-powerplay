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

        register(driveSubsystem)
        //register(armSubsystem)
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

            wait(1.0)

            requestOpModeStop()
        }
    }

    protected open suspend fun executeInstructions() {
        /*drive(Direction.FORWARD, 0.25)
        positionTurret(60)
        drive(Direction.FORWARD, 1.0)
        positionTurret(-60)
        positionArm(16.0, 5.0)
        claw(true)
        positionArm(16.0, 28.0)
        positionTurret(150)
        claw(false)
        positionArm(4.0, 4.0)*/

        when (tagId) {
            0 -> drive(Direction.LEFT, 1.0)
            2 -> drive(Direction.RIGHT, 1.0)
        }

        drive(Direction.FORWARD, 1.0)
    }

    protected suspend fun drive(direction: Direction, seconds: Double) {
        driveSubsystem.leftInput = vec2(direction)
        driveSubsystem.execute()
        wait(seconds)
        driveSubsystem.leftInput = Vector2.zero
        driveSubsystem.execute()
    }

    protected suspend fun claw(grabbing: Boolean) {
        armSubsystem.isGrabbing = grabbing
        armSubsystem.execute()
        wait(0.5)
    }

    protected suspend fun positionArm(positionX: Double, positionY: Double) {
        armSubsystem.planePosition = vec2(positionX, positionY)
        armSubsystem.execute()
        wait(2.0)
    }

    protected suspend fun positionTurret(rotateDegrees: Int) {
        armSubsystem.rotatePosition = rotateDegrees / 360.0
        armSubsystem.execute()
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