package org.firstinspires.ftc.teamcode.opmode.base

import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.hardware.Gamepad

abstract class ControllerBase: OpModeBase() {
    enum class GamepadButton {
        X, Y, A, B, LB, RB, LS, RS, DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT, BACK, START,
    }

    data class GamepadSnapshot(
        val x: Boolean = false,
        val y: Boolean = false,
        val a: Boolean = false,
        val b: Boolean = false,
        val left_bumper: Boolean = false,
        val right_bumper: Boolean = false,
        val left_stick_button: Boolean = false,
        val right_stick_button: Boolean = false,
        val dpad_up: Boolean = false,
        val dpad_down: Boolean = false,
        val dpad_left: Boolean = false,
        val dpad_right: Boolean = false,
        val back: Boolean = false,
        val start: Boolean = false,
    )

    private val Gamepad.snapshot
        get() = GamepadSnapshot(
            this.x,
            this.y,
            this.a,
            this.b,
            this.left_bumper,
            this.right_bumper,
            this.left_stick_button,
            this.right_stick_button,
            this.dpad_up,
            this.dpad_down,
            this.dpad_left,
            this.dpad_right,
            this.back,
            this.start,
        )

    val Gamepad.leftStick
        get() = vec2(this.left_stick_x, this.left_stick_y)
    val Gamepad.rightStick
        get() = vec2(this.right_stick_x, this.right_stick_y)

    val Gamepad.bumperAxis
        get() = if (this.right_bumper) 1.0 else 0.0 - if (this.left_bumper) 1.0 else 0.0
    val Gamepad.triggerAxis
        get() = (this.right_trigger - this.left_trigger).toDouble()

    fun Gamepad.axis(neg: Boolean, pos: Boolean): Double {
        var x = 0.0
        if (neg) x -= 1
        if (pos) x += 1

        return x
    }

    private var snapshot1 = GamepadSnapshot()
    private var snapshot2 = GamepadSnapshot()

    final override fun loop() {
        onLoop()
        runEvents()
        executeSubsystems()
    }

    open fun onButtonPressed(gamepad: Gamepad, button: GamepadButton) {}
    open fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {}

    private fun runEvents() {
        gamepad1?.processEvents(snapshot1)
        gamepad2?.processEvents(snapshot2)

        if (gamepad1 != null) snapshot1 = gamepad1.snapshot
        if (gamepad2 != null) snapshot2 = gamepad2.snapshot
    }

    private fun Gamepad.processEvents(snapshot: GamepadSnapshot) {
        event(GamepadButton.X, x, snapshot.x)
        event(GamepadButton.Y, y, snapshot.y)
        event(GamepadButton.A, a, snapshot.a)
        event(GamepadButton.B, b, snapshot.b)
        event(GamepadButton.LB, left_bumper, snapshot.left_bumper)
        event(GamepadButton.RB, right_bumper, snapshot.right_bumper)
        event(GamepadButton.LS, left_stick_button, snapshot.left_stick_button)
        event(GamepadButton.RS, right_stick_button, snapshot.right_stick_button)
        event(GamepadButton.DPAD_UP, dpad_up, snapshot.dpad_up)
        event(GamepadButton.DPAD_DOWN, dpad_down, snapshot.dpad_down)
        event(GamepadButton.DPAD_LEFT, dpad_left, snapshot.dpad_left)
        event(GamepadButton.DPAD_RIGHT, dpad_right, snapshot.dpad_right)
        event(GamepadButton.START, start, snapshot.start)
        event(GamepadButton.BACK, back, snapshot.back)
    }

    private fun Gamepad.event(button: GamepadButton, current: Boolean, snapshot: Boolean) {
        if (current != snapshot) {
            if (current) {
                onButtonPressed(this, button)
            }
            else {
                onButtonReleased(this, button)
            }
        }
    }
}