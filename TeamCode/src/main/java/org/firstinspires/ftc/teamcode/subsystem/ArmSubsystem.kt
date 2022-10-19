package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class ArmSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    var extendPos = 0

    var pivotPower = 0.0
    var rotatePower = 0.0

    val velocity
        get() = extendMotor.power

    private lateinit var extendMotor: DcMotorEx
    private lateinit var pivotMotor: DcMotorEx
    private lateinit var rotateMotor: DcMotorEx

    override fun onRegister() {
        extendMotor = hardwareMap.get(DcMotorEx::class.java, "extend_motor")
        pivotMotor = hardwareMap.get(DcMotorEx::class.java, "pivot_motor")
        rotateMotor = hardwareMap.get(DcMotorEx::class.java, "rotate_motor")

        extendMotor.direction = DcMotorSimple.Direction.REVERSE

        extendMotor.stopAndReset()
        pivotMotor.run()

        // extendMotor.velocity = 2000.0
    }

    override fun execute() {
        extendMotor.targetPosition = extendPos

        if (pivotPower != 0.0) {
            pivotMotor.power = pivotPower
            pivotMotor.run()
        }
        else {
            pivotMotor.stopAndReset()
        }

        rotateMotor.power = rotatePower
    }

    private fun DcMotor.stopAndReset() {
        this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.targetPosition = 0
        this.power = 1.0
        this.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    private fun DcMotor.run() {
        this.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }
}