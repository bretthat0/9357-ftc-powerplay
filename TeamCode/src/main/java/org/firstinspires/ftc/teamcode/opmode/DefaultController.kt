package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.ControllerBase
import org.firstinspires.ftc.teamcode.subsystem.DriveSubsystem
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad

@TeleOp
class DefaultController: ControllerBase() {
    private val driveSubsystem = DriveSubsystem(hardwareMap)

    override fun init() {
        register(driveSubsystem)
    }

    override fun loop() {
        driveSubsystem.leftInput = gamepad1.leftStick
        driveSubsystem.rightInput = gamepad1.rightStick

        runEvents()
        executeSubsystems()
    }

    override fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {
        if (button == GamepadButton.A) {
            stop()
        }
    }
}