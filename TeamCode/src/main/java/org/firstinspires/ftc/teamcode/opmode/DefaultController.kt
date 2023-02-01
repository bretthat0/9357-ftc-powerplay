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
        armSubsystem.rotatePosition += motorDelta(gamepad2.triggerAxis)
        armSubsystem.pivotPosition += motorDelta(gamepad2.rightStick.y)
        armSubsystem.extendPosition += motorDelta(gamepad2.leftStick.y)

        armSubsystem.wristPosition += servoDelta(gamepad1.triggerAxis)
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
                    GamepadButton.A -> armSubsystem.isGrabbing = !armSubsystem.isGrabbing

                    GamepadButton.DPAD_UP -> driveSubsystem.speed = min(driveSubsystem.speed + SPEED_STEP, MAX_SPEED)
                    GamepadButton.DPAD_DOWN -> driveSubsystem.speed = max(driveSubsystem.speed - SPEED_STEP, MIN_SPEED)
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