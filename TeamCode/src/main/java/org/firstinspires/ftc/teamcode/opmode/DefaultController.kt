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

        telemetry.addLine(""+armSubsystem.velocity)
        telemetry.update()
    }

    private fun drive() {
        mecanumSubsystem.leftInput = gamepad1.leftStick
        mecanumSubsystem.rightInput = gamepad1.rightStick
    }

    private fun arm() {
        armSubsystem.extendPos = if (gamepad1.a) 300 else 0

        if (gamepad1.x)
            armSubsystem.pivotPower = 1.0
        else if (gamepad1.y)
            armSubsystem.pivotPower = -1.0

        armSubsystem.rotatePower = gamepad1.triggerAxis
    }
}