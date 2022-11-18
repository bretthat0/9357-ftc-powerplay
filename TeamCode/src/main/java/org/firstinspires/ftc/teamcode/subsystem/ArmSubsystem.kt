package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.acos

class ArmSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    var position = Vector3.zero

    private lateinit var extendMotor: DcMotorEx
    private lateinit var pivotMotor: DcMotorEx
    //private lateinit var rotateMotor: DcMotorEx

    override fun onRegister() {
        extendMotor = hardwareMap.get(DcMotorEx::class.java, "extend_motor")
        pivotMotor = hardwareMap.get(DcMotorEx::class.java, "pivot_motor")
        ///rotateMotor = hardwareMap.get(DcMotorEx::class.java, "rotate_motor")

        // pivotMotor.direction = DcMotorSimple.Direction.REVERSE

        extendMotor.power = 0.0
        pivotMotor.power = 0.0
        //rotateMotor.power = 0.0

        extendMotor.velocity = 0.0
        pivotMotor.velocity = 0.0
        //rotateMotor.velocity = 0.0

        extendMotor.stopAndReset()
        pivotMotor.stopAndReset()
        //rotateMotor.stopAndReset()
    }

    override fun execute() {
        extendMotor.power = 1.0
        pivotMotor.power = 1.0
        //rotateMotor.power = 1.0

        val turretPos = vec2(position.x, position.z)
        val theta =
            if (turretPos.magnitude > 0.0)
                acos((turretPos dot Vector2.up) / turretPos.magnitude)
            else 0.0

        val planarPos = vec2(vec2(position.x, position.z).magnitude, position.y)
        val ik = DoubleJointIK(planarPos, ARM_LENGTH)

        val extendPos = toRevolutions(ik.q1 + ik.q2)
        val pivotPos = toRevolutions(Math.PI - ik.q1)
        val rotatePos = toRevolutions(theta)

        extendMotor.targetPosition = toTicks(extendPos, EXTEND_TPR)
        pivotMotor.targetPosition = toTicks(pivotPos, PIVOT_TPR)
        //rotateMotor.targetPosition = toTicks(rotatePos, ROTATE_TPR)

        extendMotor.velocity = toRadiansPerMin(MAX_EXTEND_SPEED, EXTEND_MOTOR_RPM)
        pivotMotor.velocity = toRadiansPerMin(MAX_PIVOT_SPEED, PIVOT_MOTOR_RPM)
        //rotateMotor.velocity = toRadiansPerMin(MAX_ROTATE_SPEED, ROTATE_MOTOR_RPM)
    }

    fun doTelemetry(telemetry: Telemetry) {
        telemetry.addLine("extend motor target: " + extendMotor.targetPosition)
        telemetry.addLine("extend motor current: " + extendMotor.currentPosition)
        telemetry.addLine("pivot motor target: " + pivotMotor.targetPosition)
        telemetry.addLine("pivot motor current: " + pivotMotor.currentPosition)
    }

    private fun toRadiansPerMin(percent: Double, rpm: Double): Double = percent * 2 * Math.PI * rpm
    private fun toRevolutions(radians: Double): Double = radians / (2 * Math.PI)
    private fun toTicks(revolutions: Double, tpr: Double): Int = (revolutions * tpr).toInt()

    private fun DcMotorEx.stopAndReset() {
        this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.targetPosition = 0
        this.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    companion object {
        // RPM
        const val EXTEND_MOTOR_RPM: Double = 312.0
        const val PIVOT_MOTOR_RPM: Double = 312.0
        const val ROTATE_MOTOR_RPM: Double = 125.0

        // SPEED
        const val MAX_EXTEND_SPEED: Double = 0.1
        const val MAX_PIVOT_SPEED: Double = 0.1
        const val MAX_ROTATE_SPEED: Double = 0.1

        // MOTOR GEAR RATIO
        const val EXTEND_MOTOR_GEAR_RATIO: Double = 263.7
        const val PIVOT_MOTOR_GEAR_RATIO: Double = 263.7
        const val ROTATE_MOTOR_GEAR_RATIO: Double = 72.1

        // GEAR RATIO
        const val EXTEND_GEAR_RATIO: Double = 1.0
        const val PIVOT_GEAR_RATIO: Double = 34.0 / 16.0
        const val ROTATE_GEAR_RATIO: Double = 54.0 / 16.0

        // MOTOR TICKS PER REVOLUTION
        const val EXTEND_MOTOR_TPR: Double = 7.0 * PIVOT_MOTOR_GEAR_RATIO
        const val PIVOT_MOTOR_TPR: Double = 7.0 * PIVOT_MOTOR_GEAR_RATIO
        const val ROTATE_MOTOR_TPR: Double = 288.0

        // TICKS PER REVOLUTION
        const val EXTEND_TPR: Double = EXTEND_MOTOR_TPR * EXTEND_GEAR_RATIO
        const val PIVOT_TPR: Double = PIVOT_MOTOR_TPR * PIVOT_GEAR_RATIO
        const val ROTATE_TPR: Double = ROTATE_MOTOR_TPR * ROTATE_GEAR_RATIO

        // MISC.
        const val ARM_LENGTH: Double = 17.0 * 2.0
    }
}