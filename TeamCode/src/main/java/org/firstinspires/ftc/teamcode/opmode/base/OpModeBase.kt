package org.firstinspires.ftc.teamcode.opmode.base

import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import java.util.*

abstract class OpModeBase: OpMode() {
    private val subsystems = ArrayList<SubsystemBase>()

    fun register(subsystem: SubsystemBase) {
        subsystems.add(subsystem)
        subsystem.onRegister()
    }

    fun deregister(subsystem: SubsystemBase) {
        subsystems.remove(subsystem)
        subsystem.onDeregister()
    }

    fun executeSubsystems() {
        for (s in subsystems) {
            s.execute()
        }
    }
}