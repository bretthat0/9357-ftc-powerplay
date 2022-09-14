package org.firstinspires.ftc.teamcode.subsystem.base

abstract class SubsystemBase {
    open fun onRegister() {}
    open fun onDeregister() {}

    abstract fun execute()
}