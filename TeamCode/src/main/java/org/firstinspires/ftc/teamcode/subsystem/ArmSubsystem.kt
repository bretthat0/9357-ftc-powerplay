package org.firstinspires.ftc.teamcode.subsystem

import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.acos
import kotlin.math.max

class ArmSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    enum class Mode {
        Plane, Manual
    }

    var mode = Mode.Plane

    var planePosition = Vector2.one
    var manualPosition = Vector2.zero
    var rotatePosition = 0.0

    var wristPosition = 0.0
    var isGrabbing = false

    private var debugQ1 = 0.0
    private var debugQ2 = 0.0

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

        extendMotor.direction = DcMotorSimple.Direction.REVERSE
        pivotMotor.direction = DcMotorSimple.Direction.REVERSE
        rotateServo.direction = Servo.Direction.REVERSE

        extendMotor.velocity = 0.0
        pivotMotor.velocity = 0.0
        rotateMotor.velocity = 0.0

        extendMotor.stopAndReset()
        pivotMotor.stopAndReset()
        rotateMotor.stopAndReset()
    }

    override fun execute() {
        extendMotor.velocity = toRadiansPerSec(MAX_EXTEND_SPEED, EXTEND_MOTOR_RPM)
        pivotMotor.velocity = toRadiansPerSec(MAX_PIVOT_SPEED, PIVOT_MOTOR_RPM)
        rotateMotor.velocity = toRadiansPerSec(MAX_ROTATE_SPEED, ROTATE_MOTOR_RPM)

        when (mode) {
            Mode.Plane -> movePlane()
            Mode.Manual -> moveManual()
        }

        rotateMotor.targetPosition = toTicks(rotatePosition, ROTATE_TPR)

        claw()
    }

    fun doTelemetry(telemetry: Telemetry) {
        telemetry.addLine("(ARM) Mode: $mode")
        telemetry.addLine("(ARM) IK target: (${planePosition.x}, ${planePosition.y})")
        telemetry.addLine("(ARM) IK angles: (q1: $debugQ1, q2: $debugQ2)")
        telemetry.addLine("(ARM) Arm ticks: (pivot: ${pivotMotor.targetPosition}, extend: ${extendMotor.targetPosition})")
    }

    fun toggleMode() {
        mode = when (mode) {
            Mode.Plane -> Mode.Manual
            Mode.Manual -> Mode.Plane
        }
    }

    private fun movePlane() {
        val ik = DoubleJointIK(planePosition, ARM_LENGTH)

        // TODO: OFFSET ZERO

        val extendPosition = toRevolutions(ik.q1 + ik.q2)
        val pivotPosition = toRevolutions(Math.PI - ik.q1)

        extendMotor.targetPosition = toTicks(extendPosition, EXTEND_TPR)
        pivotMotor.targetPosition = toTicks(pivotPosition, PIVOT_TPR)

        debugQ1 = ik.q1
        debugQ2 = ik.q2
    }

    private fun moveManual() {
        val extendPosition = manualPosition.x
        val pivotPosition = manualPosition.y

        extendMotor.targetPosition = toTicks(extendPosition, EXTEND_TPR)
        pivotMotor.targetPosition = toTicks(pivotPosition, PIVOT_TPR)
    }

    private fun claw() {
        rotateServo.position = wristPosition
        grabServo.position = if (isGrabbing) 0.4 else 0.0
    }

    private fun toRadiansPerSec(factor: Double, rpm: Double): Double = factor * 2 * Math.PI * rpm * 60
    private fun toRevolutions(radians: Double): Double = radians / (2 * Math.PI)
    private fun toTicks(revolutions: Double, tpr: Double): Int = (revolutions * tpr).toInt()

    private fun DcMotorEx.stopAndReset() {
        this.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        this.targetPosition = 0
        this.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    companion object {
        // RPM
        const val EXTEND_MOTOR_RPM: Double = 30.0
        const val PIVOT_MOTOR_RPM: Double = 30.0
        const val ROTATE_MOTOR_RPM: Double = 312.0

        // SPEED
        const val MAX_EXTEND_SPEED: Double = 1.0
        const val MAX_PIVOT_SPEED: Double = 1.0
        const val MAX_ROTATE_SPEED: Double = 1.0

        // GEAR RATIO
        const val EXTEND_GEAR_RATIO: Double = 34.0 / 16.0
        const val PIVOT_GEAR_RATIO: Double = 34.0 / 16.0
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