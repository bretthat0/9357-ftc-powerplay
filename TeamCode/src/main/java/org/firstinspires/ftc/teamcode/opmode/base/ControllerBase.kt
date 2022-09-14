package org.firstinspires.ftc.teamcode.opmode.base

import org.firstinspires.ftc.teamcode.util.*
import com.qualcomm.robotcore.hardware.Gamepad

abstract class ControllerBase: OpModeBase() {
    enum class GamepadButton {
        X, Y, A, B, LB, RB, LS, RS, DPAD_UP, DPAD_DOWN, DPAD_LEFT, DPAD_RIGHT, BACK, START,
    }

    data class GamepadSnapshot(
        val x: Boolean,
        val y: Boolean,
        val a: Boolean,
        val b: Boolean,
        val left_bumper: Boolean,
        val right_bumper: Boolean,
        val left_stick_button: Boolean,
        val right_stick_button: Boolean,
        val dpad_up: Boolean,
        val dpad_down: Boolean,
        val dpad_left: Boolean,
        val dpad_right: Boolean,
        val back: Boolean,
        val start: Boolean,
    )

    val Gamepad.snapshot
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

    private var snapshot1 = gamepad1.snapshot
    private var snapshot2 = gamepad2.snapshot

    open fun onButtonPressed(gamepad: Gamepad, button: GamepadButton) {}
    open fun onButtonReleased(gamepad: Gamepad, button: GamepadButton) {}

    fun runEvents() {
        gamepad1.processEvents(snapshot1)
        gamepad2.processEvents(snapshot2)

        snapshot1 = gamepad1.snapshot
        snapshot2 = gamepad2.snapshot
    }

    private fun Gamepad.processEvents(snapshot: GamepadSnapshot) {
        event(GamepadButton.X, x, snapshot.x)
        event(GamepadButton.Y, y, snapshot.y)
        event(GamepadButton.A, a, snapshot.a)
        event(GamepadButton.B, b, snapshot.b)
        event(GamepadButton.LB, left_bumper, snapshot.left_bumper)
        event(GamepadButton.RB, right_bumper, snapshot.right_bumper)
        event(GamepadButton.LS, left_stick_button, snapshot.right_stick_button)
        event(GamepadButton.RS, right_stick_button, snapshot.left_stick_button)
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