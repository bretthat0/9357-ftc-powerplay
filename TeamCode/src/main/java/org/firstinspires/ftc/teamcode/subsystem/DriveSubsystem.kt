package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class DriveSubsystem(private var hardwareMap: HardwareMap): SubsystemBase() {
    var leftInput = Vector2.zero
    var rightInput = Vector2.zero

    private lateinit var leftMotor: DcMotor
    private lateinit var rightMotor: DcMotor
    private lateinit var strafeMotor: DcMotor

    override fun onRegister() {
        leftMotor = hardwareMap.dcMotor.get("left_motor")
        rightMotor = hardwareMap.dcMotor.get("right_motor")
        strafeMotor = hardwareMap.dcMotor.get("strafe_motor")
    }

    override fun execute() {
        leftMotor.power = rightInput.x - leftInput.y
        rightMotor.power = rightInput.x + leftInput.y
        strafeMotor.power = leftInput.x
    }
}