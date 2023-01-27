package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*


class MecanumDriveSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    var speed = 0.55
    var leftInput = Vector2.zero
    var rightInput = Vector2.zero

    private lateinit var frontLeftMotor: DcMotor
    private lateinit var frontRightMotor: DcMotor
    private lateinit var backLeftMotor: DcMotor
    private lateinit var backRightMotor: DcMotor

    override fun onRegister() {
        frontLeftMotor = hardwareMap.dcMotor.get("front_left_motor")
        frontRightMotor = hardwareMap.dcMotor.get("front_right_motor")
        backLeftMotor = hardwareMap.dcMotor.get("back_left_motor")
        backRightMotor = hardwareMap.dcMotor.get("back_right_motor")

        frontLeftMotor.direction = DcMotorSimple.Direction.REVERSE
        backLeftMotor.direction = DcMotorSimple.Direction.REVERSE
    }

    fun doTelemetry(telemetry: Telemetry) {
        // TODO
    }

    override fun execute() {
        val y = -leftInput.y
        val x = leftInput.x
        val rx = rightInput.x

        frontLeftMotor.power = (y + x + rx) * speed
        frontRightMotor.power = (y - x - rx) * speed
        backLeftMotor.power = (y - x + rx) * speed
        backRightMotor.power = (y + x - rx) * speed
    }
}