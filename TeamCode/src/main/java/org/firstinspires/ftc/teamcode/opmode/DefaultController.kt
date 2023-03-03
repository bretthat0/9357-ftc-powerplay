package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.ControllerBase
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem
import org.firstinspires.ftc.teamcode.util.*

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

        // For testing
        armSubsystem.planePosition = vec2(17.0, 17.0)

        register(driveSubsystem)
        register(armSubsystem)
    }

    override fun onLoop() {
        drive()
        arm()

        telemetry.addLine("Game harder >:(")
        telemetry.addLine("Slow mode: $slowMode")
        driveSubsystem.doTelemetry(telemetry)
        armSubsystem.doTelemetry(telemetry)
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick
        driveSubsystem.rightInput = gamepad1.rightStick

        driveSubsystem.speed = clamp(driveSubsystem.speed, MIN_SPEED, MAX_SPEED)
    }

    private fun arm() {
        val delX = inputDelta(gamepad2.rightStick.y * slowFactor)
        val delY = inputDelta(gamepad2.leftStick.y * slowFactor)

        when (armSubsystem.mode) {
            ArmSubsystem.Mode.Plane -> {
                armSubsystem.planePosition.x += delX * ArmSubsystem.ARM_LENGTH
                armSubsystem.planePosition.y += delY * ArmSubsystem.ARM_LENGTH
            }
            ArmSubsystem.Mode.Manual -> {
                armSubsystem.manualPosition.x += delX
                armSubsystem.manualPosition.y += delY
            }
        }

        armSubsystem.rotatePosition += inputDelta(gamepad2.triggerAxis * slowFactor * 0.5)
        armSubsystem.wristPosition += inputDelta(gamepad1.triggerAxis)

        // TODO: Make this more robust
        armSubsystem.planePosition.clampMagnitude(ArmSubsystem.ARM_LENGTH)
        armSubsystem.planePosition = clamp(armSubsystem.planePosition, vec2(1.0, -2.0), Vector2.inf)
        armSubsystem.wristPosition = clamp(armSubsystem.wristPosition, 0.0, 1.0)
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
                    GamepadButton.DPAD_UP -> driveSubsystem.speed += SPEED_STEP
                    GamepadButton.DPAD_DOWN -> driveSubsystem.speed -= SPEED_STEP
                    else -> {}
                }
            }
            gamepad2 -> {
                when (button) {
                    GamepadButton.RB -> slowMode = !slowMode
                    GamepadButton.RS -> armSubsystem.toggleMode()
                    GamepadButton.DPAD_UP -> armSubsystem.planePosition = vec2(4.0, 32.0)
                    GamepadButton.DPAD_DOWN -> armSubsystem.planePosition = vec2(4.0, 0.0)
                    GamepadButton.DPAD_RIGHT -> armSubsystem.rotatePosition += 0.5
                    GamepadButton.DPAD_LEFT -> armSubsystem.rotatePosition -= 0.5
                    else -> {}
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