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

        register(driveSubsystem)
        register(armSubsystem)
    }

    override fun onLoop() {
        drive()
        arm()

        telemetry.addLine("Game harder >:(")
        //driveSubsystem.doTelemetry(telemetry)
        telemetry.addLine("\nARM:")
        armSubsystem.doTelemetry(telemetry)
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick * speed
        driveSubsystem.rightInput = gamepad1.rightStick * speed
    }

    private fun arm() {
        var relY = motorDelta(gamepad2.triggerAxis)

        when (turret) {
            true -> offsetArm(motorDelta(gamepad2.bumperAxis), relY, 0.0)
            false -> offsetArm(0.0, relY, motorDelta(gamepad2.bumperAxis))
        }

        armSubsystem.wristPosition += servoDelta(gamepad2.axis(gamepad2.y, gamepad2.b))
    }

    private fun offsetArm(relX: Double, relY: Double, relZ: Double) {
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
        const val INPUT_DELTA = 0.48
        const val SPEED_STEP = 0.15

        const val MAX_SPEED = 1.0
        const val MIN_SPEED = 0.1
    }
}