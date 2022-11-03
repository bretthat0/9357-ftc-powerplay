package org.firstinspires.ftc.teamcode.util

import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.sin
import kotlin.math.cos

data class DoubleJointIK(var position: Vector2, var armLength1: Double, var armLength2: Double) {
    val x: Double
        get() = position.x
    val y: Double
        get() = position.y
    val a1: Double
        get() = armLength1
    val a2: Double
        get() = armLength2

    val q1: Double
        get() = atan(y / x) - atan((a2 * sin(q2)) / (a1 + a2 * cos(q2)))
    val q2: Double
        get() = -acos((x*x + y*y - a1*a1 - a2*a2) / (2 * a1 * a2))
}