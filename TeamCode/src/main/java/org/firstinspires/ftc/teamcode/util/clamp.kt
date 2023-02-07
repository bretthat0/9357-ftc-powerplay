package org.firstinspires.ftc.teamcode.util

import kotlin.math.max
import kotlin.math.min

fun clamp(n: Int, lower: Int, upper: Int) = max(min(n, upper), lower)
fun clamp(n: Double, lower: Double, upper: Double) = max(min(n, upper), lower)
fun clamp(n: Float, lower: Float, upper: Float) = max(min(n, upper), lower)
fun clamp(v: Vector2, lower: Vector2, upper: Vector2) = vec2(clamp(v.x, lower.x, upper.x), clamp(v.y, lower.y, upper.y))

fun Vector2.clampMagnitude(upper: Double) {
    if (magnitude > upper) {
        this *= upper / magnitude
    }
}