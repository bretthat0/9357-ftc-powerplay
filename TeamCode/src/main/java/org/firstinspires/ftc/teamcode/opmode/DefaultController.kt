package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.ControllerBase
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem
import org.firstinspires.ftc.teamcode.subsystem.BallDriveSubsystem
import org.firstinspires.ftc.teamcode.util.vec3
import kotlin.math.max
import kotlin.math.min

@TeleOp(name = "Default Controller")
class DefaultController: ControllerBase() {
    private lateinit var driveSubsystem: MecanumDriveSubsystem
    private lateinit var armSubsystem: ArmSubsystem

    private var slowMode = false
    private val slowFactor: Double
        get() = if (slowMode) 0.3 else 1.0

    override fun onInit() {
        driveSubsystem = MecanumDriveSubsystem(hardwareMap)
        armSubsystem = ArmSubsystem(hardwareMap)

        armSubsystem.mode = ArmSubsystem.Mode.Manual

        register(driveSubsystem)
        register(armSubsystem)
    }

    override fun onLoop() {
        drive()
        arm()

        telemetry.addLine("Game harder >:(")
        telemetry.addLine("Turret slow mode: $slowMode")
        /*driveSubsystem.doTelemetry(telemetry)
        armSubsystem.doTelemetry(telemetry)*/
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick
        driveSubsystem.rightInput = gamepad1.rightStick
    }

    private fun arm() {
        armSubsystem.rotatePosition += inputDelta(gamepad2.triggerAxis * slowFactor * 0.5)
        armSubsystem.pivotPosition += inputDelta(gamepad2.rightStick.y * slowFactor)
        armSubsystem.extendPosition += inputDelta(gamepad2.leftStick.y * slowFactor)

        armSubsystem.wristPosition += inputDelta(gamepad1.triggerAxis)

        // Constraints
        armSubsystem.pivotPosition = max(armSubsystem.pivotPosition, 0.0)
        armSubsystem.wristPosition = max(min(armSubsystem.wristPosition, 1.0), -1.0)
    }

    private fun inputDelta(x: Double): Double {
        return x * INPUT_DELTA * deltaTime
    }

    override fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {
        when (gamepad) {
            gamepad1 -> {
                when (button) {
                    GamepadButton.A -> armSubsystem.isGrabbing = !armSubsystem.isGrabbing
                    GamepadButton.Y -> armSubsystem.wristPosition = 0.0

                    GamepadButton.DPAD_UP -> driveSubsystem.speed = min(driveSubsystem.speed + SPEED_STEP, MAX_SPEED)
                    GamepadButton.DPAD_DOWN -> driveSubsystem.speed = max(driveSubsystem.speed - SPEED_STEP, MIN_SPEED)
                }
            }
            gamepad2 -> {
                when (button) {
                    GamepadButton.RB -> slowMode = !slowMode
                }
            }
        }
    }

    companion object {
        const val INPUT_DELTA = 0.65
        const val SPEED_STEP = 0.15

        const val MAX_SPEED = 1.0
        const val MIN_SPEED = 0.1
    }
}