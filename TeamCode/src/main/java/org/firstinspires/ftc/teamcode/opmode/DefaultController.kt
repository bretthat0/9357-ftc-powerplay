package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.ControllerBase
import org.firstinspires.ftc.teamcode.subsystem.MecanumDriveSubsystem
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.subsystem.ArmSubsystem
import org.firstinspires.ftc.teamcode.subsystem.BallDriveSubsystem
import org.firstinspires.ftc.teamcode.util.vec3

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
        arm();

        telemetry.addLine("Game harder >:(")
        telemetry.addLine("delta: $deltaTime")
        armSubsystem.doTelemetry(telemetry)
        telemetry.update()
    }

    private fun drive() {
        driveSubsystem.leftInput = gamepad1.leftStick
        driveSubsystem.rightInput = gamepad1.rightStick
    }

    private fun arm() {
        //armSubsystem.position = vec3(0.0, 10.0, 10.0)

        armSubsystem.extendPosition += gamepad1.triggerAxis * 0.05 * deltaTime
        armSubsystem.pivotPosition += gamepad1.bumperAxis * 0.05 * deltaTime

        armSubsystem.wristPosition = if (gamepad1.x) 0.25 else 0.0
        armSubsystem.isGrabbing = gamepad1.a
    }
}