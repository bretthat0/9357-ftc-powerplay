package org.firstinspires.ftc.teamcode.opmode

import org.firstinspires.ftc.teamcode.opmode.base.AutoBase
import org.firstinspires.ftc.teamcode.subsystem.BallDriveSubsystem
import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name="Default Auto")
class DefaultAuto: AutoBase() {
    private lateinit var driveSubsystem: BallDriveSubsystem

    override fun onInit() {
        driveSubsystem = BallDriveSubsystem(hardwareMap)

        // register(driveSubsystem)
    }

    override suspend fun onStart() {
        driveTime(Direction.FORWARD, 10.0)
        // drive(Direction.LEFT, 50.0)
    }

    private suspend fun driveTime(dir: Direction, time: Double) {
        driveSubsystem.leftInput = dir.toVector2()
        wait(time)
        driveSubsystem.leftInput = Vector2.zero
    }
}