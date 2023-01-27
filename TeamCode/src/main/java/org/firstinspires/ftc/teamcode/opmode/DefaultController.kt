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
        driveSubsystem.doTelemetry(telemetry)
        telemetry.addLine("\nARM:")
        armSubsystem.doTelemetry(telemetry)
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick
        driveSubsystem.rightInput = gamepad1.rightStick
    }

    private fun arm() {
        var relX = motorDelta(gamepad2.leftStick.x)
        var relY = motorDelta(gamepad2.triggerAxis)
        var relZ = motorDelta(gamepad2.bumperAxis)

        when (armSubsystem.mode) {
            ArmSubsystem.Mode.WorldSpace -> {
                armSubsystem.position.x += relX
                armSubsystem.position.y += relY
                armSubsystem.position.z += relZ
            }
            ArmSubsystem.Mode.Manual -> {
                armSubsystem.rotatePosition += relX
                armSubsystem.pivotPosition += relY
                armSubsystem.extendPosition += relZ
            }
        }

        armSubsystem.wristPosition += servoDelta(gamepad1.axis(gamepad1.y, gamepad1.b))
    }

    private fun motorDelta(x: Double): Double {
        return x * INPUT_DELTA * deltaTime
    }

    private fun servoDelta(x: Double): Double {
        return motorDelta(x) / 4.0
    }

    override fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {
        when (gamepad) {
            gamepad1 -> {
                when (button) {
                    GamepadButton.DPAD_UP -> driveSubsystem.speed = min(driveSubsystem.speed + SPEED_STEP, MAX_SPEED)
                    GamepadButton.DPAD_DOWN -> driveSubsystem.speed = max(driveSubsystem.speed - SPEED_STEP, MIN_SPEED)
                }
            }
            gamepad2 -> {
                when (button) {
                    GamepadButton.A -> armSubsystem.isGrabbing = !armSubsystem.isGrabbing
                    GamepadButton.RS -> armSubsystem.toggleMode()

                    // TODO: Preset Arm Positions
                    GamepadButton.DPAD_UP -> armSubsystem.position = vec3(0.0, 0.0, 0.0)
                    GamepadButton.DPAD_DOWN -> armSubsystem.position = vec3(0.0, 0.0, 0.0)
                    GamepadButton.DPAD_LEFT -> armSubsystem.position = vec3(0.0, 0.0, 0.0)
                    GamepadButton.DPAD_RIGHT -> armSubsystem.position = vec3(0.0, 0.0, 0.0)
                }
            }
        }
    }

    companion object {
        const val INPUT_DELTA = 0.24
        const val SPEED_STEP = 0.15

        const val MAX_SPEED = 1.0
        const val MIN_SPEED = 0.1
    }
}