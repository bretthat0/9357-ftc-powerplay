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

    private var speed = (MAX_SPEED + MIN_SPEED) / 2.0
    private var turret = false

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
        //driveSubsystem.doTelemetry(telemetry)
        armSubsystem.doTelemetry(telemetry)
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick * speed
        driveSubsystem.rightInput = gamepad1.rightStick * speed
    }

    private fun arm() {
        when (armSubsystem.mode) {
            ArmSubsystem.Mode.WorldSpace -> {
                armSubsystem.position.y += gamepad2.triggerAxis * INPUT_DELTA * deltaTime

                when (turret) {
                    true -> armSubsystem.position.x += gamepad2.bumperAxis * INPUT_DELTA * deltaTime
                    false -> armSubsystem.position.z += gamepad2.bumperAxis * INPUT_DELTA * deltaTime
                }
            }
            ArmSubsystem.Mode.Manual -> {
                armSubsystem.pivotPosition += gamepad2.triggerAxis * INPUT_DELTA * deltaTime

                when (turret) {
                    true -> armSubsystem.rotatePosition += gamepad2.bumperAxis * INPUT_DELTA * deltaTime
                    false -> armSubsystem.extendPosition += gamepad2.bumperAxis * INPUT_DELTA * deltaTime
                }
            }
        }
    }

    override fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {
        when (gamepad) {
            gamepad1 -> {
                when (button) {
                    GamepadButton.DPAD_UP -> min(speed + SPEED_STEP, MAX_SPEED)
                    GamepadButton.DPAD_DOWN -> max(speed - SPEED_STEP, MIN_SPEED)
                }
            }
            gamepad2 -> {
                when (button) {
                    GamepadButton.A -> armSubsystem.isGrabbing = !armSubsystem.isGrabbing
                    GamepadButton.X -> turret = !turret
                    GamepadButton.RS -> {
                        when (armSubsystem.mode) {
                            ArmSubsystem.Mode.WorldSpace -> ArmSubsystem.Mode.Manual
                            ArmSubsystem.Mode.Manual -> ArmSubsystem.Mode.WorldSpace
                        }
                    }

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
        const val INPUT_DELTA = 0.12
        const val SPEED_STEP = 0.15

        const val MAX_SPEED = 1.0
        const val MIN_SPEED = 0.1
    }
}