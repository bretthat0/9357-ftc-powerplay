package org.firstinspires.ftc.teamcode.util

import kotlin.math.sqrt

fun vec2(x: Double, y: Double) = Vector2(x, y)
fun vec2(x: Float, y: Float) = Vector2(x.toDouble(), y.toDouble())

data class Vector2(var x: Double, var y: Double) {
    companion object {
        val zero = Vector2(0.0, 0.0)
    }

    var xF: Float
        get() = x.toFloat()
        set(value) { x = value.toDouble() }
    var yF: Float
        get() = y.toFloat()
        set(value) { y = value.toDouble() }

    val magnitude
        get() = sqrt(x*x + y*y)
    val unit
        get() = this / magnitude

    operator fun unaryMinus() = Vector2(-x, -y)

    operator fun plus(rhs: Vector2) = Vector2(x + rhs.x, y + rhs.y)
    operator fun minus(rhs: Vector2) = Vector2(x - rhs.x, y - rhs.y)
    operator fun times(rhs: Double) = Vector2(x * rhs, y * rhs)
    operator fun div(rhs: Double) = Vector2(x / rhs, y / rhs)

    operator fun times(rhs: Float) = times(rhs.toDouble())
    operator fun div(rhs: Float) = div(rhs.toDouble())

    operator fun plusAssign(rhs: Vector2) {
        x += rhs.x
        y += rhs.y
    }
    operator fun minusAssign(rhs: Vector2) {
        x -= rhs.x
        y -= rhs.y
    }
    operator fun timesAssign(rhs: Double) {
        x *= rhs
        y *= rhs
    }
    operator fun divAssign(rhs: Double) {
        x /= rhs
        y /= rhs
    }

    operator fun timesAssign(rhs: Float) = timesAssign(rhs.toDouble())
    operator fun divAssign(rhs: Float) = divAssign(rhs.toDouble())

    infix fun dot(rhs: Vector2) = x * rhs.x + y * rhs.y
}