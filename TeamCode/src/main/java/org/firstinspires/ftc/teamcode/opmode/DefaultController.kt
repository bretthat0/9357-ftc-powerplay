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
        //armSubsystem.position = vec3(0.0, 10.0, 10.0)

        if (gamepad2.y) {
            armSubsystem.rotatePosition += gamepad2.triggerAxis * INPUT_DELTA * deltaTime
            armSubsystem.wristPosition += gamepad2.bumperAxis * INPUT_DELTA * deltaTime
        }
        else {
            armSubsystem.extendPosition += gamepad2.triggerAxis * INPUT_DELTA * deltaTime
            armSubsystem.pivotPosition += gamepad2.bumperAxis * INPUT_DELTA * deltaTime
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
                    GamepadButton.RS -> {
                        when (armSubsystem.mode) {
                            ArmSubsystem.Mode.WorldSpace -> ArmSubsystem.Mode.Manual
                            ArmSubsystem.Mode.Manual -> ArmSubsystem.Mode.WorldSpace
                        }
                    }
                    // TODO: Toggle between arm/turret/claw
                    // TODO: Dpad Preset Arm Positions
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