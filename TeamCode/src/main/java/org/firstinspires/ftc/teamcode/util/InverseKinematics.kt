package org.firstinspires.ftc.teamcode.util

import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.sin
import kotlin.math.cos

fun DoubleJointIK(position: Vector2, extent: Double) = DoubleJointIK(position, extent / 2, extent / 2)
fun DoubleJointIK(x: Double, y: Double, extent: Double) = DoubleJointIK(vec2(x, y), extent)

data class DoubleJointIK(var position: Vector2, var armLength1: Double, var armLength2: Double) {
    val x = position.x
    val y = position.y
    val a1 = armLength1
    val a2 = armLength2

    val extent: Double
        get() = a1 + a2

    val q2 = -acos((x*x + y*y - a1*a1 - a2*a2) / (2 * a1 * a2))
    val q1 = atan(y / x) - atan((a2 * sin(q2)) / (a1 + a2 * cos(q2)))
}