package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.ControllerBase
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem

@TeleOp(name = "Default Controller")
class DefaultController: ControllerBase() {
    private lateinit var mecanumSubsystem: MecanumDriveSubsystem
    private lateinit var armSubsystem: ArmSubsystem

    override fun onInit() {
        mecanumSubsystem = MecanumDriveSubsystem(hardwareMap)
        armSubsystem = ArmSubsystem(hardwareMap)

        // register(mecanumSubsystem)
        register(armSubsystem)
    }

    override fun onLoop() {
        drive()
        arm()

        telemetry.addLine("Game harder >:(")
        telemetry.update()
    }

    private fun drive() {
        mecanumSubsystem.leftInput = gamepad1.leftStick
        mecanumSubsystem.rightInput = gamepad1.rightStick
    }

    private fun arm() {
        // TODO
    }
}