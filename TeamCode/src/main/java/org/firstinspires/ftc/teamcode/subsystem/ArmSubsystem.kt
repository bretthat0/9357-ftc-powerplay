package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap

class ArmSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    var position = Vector3.zero

    private lateinit var extendMotor: DcMotorEx
    private lateinit var pivotMotor: DcMotorEx
    private lateinit var rotateMotor: DcMotorEx

    override fun onRegister() {
        extendMotor = hardwareMap.get(DcMotorEx::class.java, "extend_motor")
        pivotMotor = hardwareMap.get(DcMotorEx::class.java, "pivot_motor")
        rotateMotor = hardwareMap.get(DcMotorEx::class.java, "rotate_motor")

        pivotMotor.direction = DcMotorSimple.Direction.REVERSE

        extendMotor.power = 0.0
        pivotMotor.power = 0.0
        rotateMotor.power = 0.0

        extendMotor.velocity = 0.0
        pivotMotor.velocity = 0.0
        rotateMotor.velocity = 0.0

        extendMotor.stopAndReset()
        pivotMotor.stopAndReset()
        rotateMotor.stopAndReset()
    }

    override fun execute() {
        extendMotor.power = 1.0
        pivotMotor.power = 1.0
        rotateMotor.power = 1.0

        val planarPos = vec2(0.0, 0.0) // TODO

        val ik = DoubleJointIK(planarPos, ARM_LENGTH)

        val extendPos = (ik.q1 + ik.q2)
        val pivotPos = ik.q1 / (2 * Math.PI)
        val rotatePos = 0.0 // TODO

        extendMotor.targetPosition = toTicks(extendPos, EXTEND_TPR)
        pivotMotor.targetPosition = toTicks(pivotPos, PIVOT_TPR)
        rotateMotor.targetPosition = toTicks(rotatePos, ROTATE_TPR)

        extendMotor.velocity = toRadiansPerMin(MAX_EXTEND_SPEED)
        pivotMotor.velocity = toRadiansPerMin(MAX_PIVOT_SPEED)
        rotateMotor.velocity = toRadiansPerMin(MAX_ROTATE_SPEED)
    }

    private fun toRadiansPerMin(percent: Double): Double = percent * 2 * Math.PI * MOTOR_RPM
    private fun toTicks(rotations: Double, tpr: Double): Int = (rotations * tpr).toInt()

    private fun DcMotorEx.stopAndReset() {
        this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.targetPosition = 0
        this.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    companion object {
        const val MOTOR_RPM: Double = 312.0
        const val MAX_EXTEND_SPEED: Double = 0.5
        const val MAX_PIVOT_SPEED: Double = 0.8
        const val MAX_ROTATE_SPEED: Double = 0.2
        const val EXTEND_GEAR_RATIO: Double = 19.2
        const val PIVOT_GEAR_RATIO: Double = 263.7
        const val ROTATE_GEAR_RATIO: Double = 54.0 / 16.0
        const val EXTEND_MOTOR_TPR: Double = 537.7
        const val PIVOT_MOTOR_TPR: Double = 7.0 * PIVOT_GEAR_RATIO
        const val ROTATE_MOTOR_TPR: Double = 537.7
        const val EXTEND_TPR: Double = EXTEND_MOTOR_TPR * EXTEND_GEAR_RATIO
        const val PIVOT_TPR: Double = PIVOT_MOTOR_TPR * PIVOT_GEAR_RATIO
        const val ROTATE_TPR: Double = ROTATE_MOTOR_TPR * ROTATE_GEAR_RATIO
        const val ARM_LENGTH: Double = 34.0
    }
}