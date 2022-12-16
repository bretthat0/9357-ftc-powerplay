package org.firstinspires.ftc.teamcode.opmode.base

import org.firstinspires.ftc.teamcode.subsystem.base.SubsystemBase
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import java.util.*

abstract class OpModeBase: OpMode() {
    private val subsystems = ArrayList<SubsystemBase>()
    private var prevTime = System.nanoTime()

    var deltaTime: Double = 0.0
        private set

    abstract fun onInit()
    open fun onLoop() {}

    override fun init() {
        deltaTime = 5e-2;

        onInit()
    }

    override fun loop() {
        onLoop()
    }

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