package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.ControllerBase
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem
import org.firstinspires.ftc.teamcode.subsystem.BallDriveSubsystem
import org.firstinspires.ftc.teamcode.util.vec3

@TeleOp(name = "Default Controller")
class DefaultController: ControllerBase() {
    private lateinit var driveSubsystem: MecanumDriveSubsystem
    private lateinit var armSubsystem: ArmSubsystem

    private var isGrabbing = false

    override fun onInit() {
        driveSubsystem = MecanumDriveSubsystem(hardwareMap)
        armSubsystem = ArmSubsystem(hardwareMap)

        armSubsystem.mode = ArmSubsystem.Mode.Manual

        register(driveSubsystem)
        register(armSubsystem)
    }

    override fun onLoop() {
        drive()
        arm();

        telemetry.addLine("Game harder >:(")
        armSubsystem.doTelemetry(telemetry)
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick
        driveSubsystem.rightInput = gamepad1.rightStick
    }

    private fun arm() {
        //armSubsystem.position = vec3(0.0, 10.0, 10.0)

        if (gamepad2.y) {
            armSubsystem.rotatePosition += gamepad2.triggerAxis * INP_SPEED * deltaTime
            armSubsystem.wristPosition += gamepad2.bumperAxis * 0.01 * deltaTime
        }
        else {
            armSubsystem.extendPosition += gamepad2.triggerAxis * INP_SPEED * deltaTime
            armSubsystem.pivotPosition += gamepad2.bumperAxis * INP_SPEED * deltaTime
        }

        armSubsystem.isGrabbing = isGrabbing
    }

    override fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {
        if (gamepad == gamepad2) {
            if (button == GamepadButton.A) {
                isGrabbing = !isGrabbing
            }
        }
    }

    companion object {
        const val INP_SPEED = 0.12
    }
}