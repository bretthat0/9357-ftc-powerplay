package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.acos

class ArmSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    enum class Mode {
        WorldSpace, Manual
    }

    var mode = Mode.WorldSpace

    // WorldSpace
    var position = Vector3.forward

    // Manual
    var extendPosition = 0.0
    var pivotPosition = 0.0
    var rotatePosition = 0.0

    // Claw
    var wristPosition = 0.0
    var isGrabbing = false

    private lateinit var extendMotor: DcMotorEx
    private lateinit var pivotMotor: DcMotorEx
    private lateinit var rotateMotor: DcMotorEx

    private lateinit var rotateServo: Servo
    private lateinit var grabServo: Servo

    override fun onRegister() {
        extendMotor = hardwareMap.get(DcMotorEx::class.java, "extend_motor")
        pivotMotor = hardwareMap.get(DcMotorEx::class.java, "pivot_motor")
        rotateMotor = hardwareMap.get(DcMotorEx::class.java, "rotate_motor")

        rotateServo = hardwareMap.get(Servo::class.java, "rotate_servo")
        grabServo = hardwareMap.get(Servo::class.java, "grab_servo")

        //extendMotor.direction = DcMotorSimple.Direction.REVERSE
        //pivotMotor.direction = DcMotorSimple.Direction.REVERSE
        rotateServo.direction = Servo.Direction.REVERSE

        extendMotor.velocity = 0.0
        pivotMotor.velocity = 0.0
        rotateMotor.velocity = 0.0

        extendMotor.stopAndReset()
        pivotMotor.stopAndReset()
        rotateMotor.stopAndReset()
    }

    override fun execute() {
        extendMotor.velocity = toRadiansPerMin(MAX_EXTEND_SPEED, EXTEND_MOTOR_RPM)
        pivotMotor.velocity = toRadiansPerMin(MAX_PIVOT_SPEED, PIVOT_MOTOR_RPM)
        rotateMotor.velocity = toRadiansPerMin(MAX_ROTATE_SPEED, ROTATE_MOTOR_RPM)

        when (mode) {
            Mode.WorldSpace -> moveWorldSpace()
            Mode.Manual -> moveManual()
        }

        claw()
    }

    fun doTelemetry(telemetry: Telemetry) {
        telemetry.addLine("mode: $mode")
        telemetry.addLine("manual extend target: $extendPosition")
        telemetry.addLine("manual pivot target: $pivotPosition")
        telemetry.addLine("manual rotate target: $rotatePosition")
        //telemetry.addLine("IK target: (${position.x}, ${position.y}, ${position.z})")
    }

    fun toggleMode() {
        mode = when (mode) {
            Mode.WorldSpace -> Mode.Manual
            Mode.Manual -> Mode.WorldSpace
        }
    }

    private fun moveWorldSpace() {
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
        rotateMotor.targetPosition = toTicks(rotatePos, ROTATE_TPR)
    }

    private fun moveManual() {
        extendMotor.targetPosition = toTicks(extendPosition, EXTEND_TPR)
        pivotMotor.targetPosition = toTicks(pivotPosition, PIVOT_TPR)
        rotateMotor.targetPosition = toTicks(rotatePosition, ROTATE_TPR)
    }

    private fun claw() {
        rotateServo.position = wristPosition
        grabServo.position = if (isGrabbing) 0.4 else 0.0
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
        const val MAX_EXTEND_SPEED: Double = 1.0
        const val MAX_PIVOT_SPEED: Double = 1.0
        const val MAX_ROTATE_SPEED: Double = 1.0

        // MOTOR GEAR RATIO
        const val EXTEND_MOTOR_GEAR_RATIO: Double = 188.0
        const val PIVOT_MOTOR_GEAR_RATIO: Double = 188.0
        const val ROTATE_MOTOR_GEAR_RATIO: Double = 19.2

        // GEAR RATIO
        const val EXTEND_GEAR_RATIO: Double = 16.0 / 34.0
        const val PIVOT_GEAR_RATIO: Double = 16.0 / 34.0
        const val ROTATE_GEAR_RATIO: Double = 2.0 / 1.0

        // MOTOR TICKS PER REVOLUTION
        const val EXTEND_MOTOR_TPR: Double = 5281.1
        const val PIVOT_MOTOR_TPR: Double = 5281.1
        const val ROTATE_MOTOR_TPR: Double = 537.7

        // TICKS PER REVOLUTION
        const val EXTEND_TPR: Double = EXTEND_MOTOR_TPR * EXTEND_GEAR_RATIO
        const val PIVOT_TPR: Double = PIVOT_MOTOR_TPR * PIVOT_GEAR_RATIO
        const val ROTATE_TPR: Double = ROTATE_MOTOR_TPR * ROTATE_GEAR_RATIO

        // MISC.
        const val ARM_LENGTH: Double = 17.0 * 2.0
    }
}