package org.firstinspires.ftc.teamcode.util

import kotlin.math.sqrt

fun vec3(x: Double, y: Double, z: Double) = Vector3(x, y, z)
fun vec3(x: Float, y: Float, z: Float) = Vector3(x.toDouble(), y.toDouble(), z.toDouble())

data class Vector3(var x: Double, var y: Double, var z: Double) {
    companion object {
        val zero = Vector3(0.0, 0.0, 0.0)
        val right = Vector3(1.0, 0.0, 0.0)
        val up = Vector3(0.0, 1.0, 0.0)
        val forward = Vector3(0.0, 0.0, 1.0)
    }

    var xF: Float
        get() = x.toFloat()
        set(value) { x = value.toDouble() }
    var yF: Float
        get() = y.toFloat()
        set(value) { y = value.toDouble() }
    var zF: Float
        get() = z.toFloat()
        set(value) { z = value.toDouble() }

    val magnitude
        get() = sqrt(x*x + y*y + z*z)
    val unit
        get() = this / magnitude

    operator fun unaryMinus() = Vector3(-x, -y, -z)

    operator fun plus(rhs: Vector3) = Vector3(x + rhs.x, y + rhs.y, z + rhs.z)
    operator fun minus(rhs: Vector3) = Vector3(x - rhs.x, y - rhs.y, z - rhs.z)
    operator fun times(rhs: Double) = Vector3(x * rhs, y * rhs, z * rhs)
    operator fun div(rhs: Double) = Vector3(x / rhs, y / rhs, z / rhs)

    operator fun times(rhs: Float) = times(rhs.toDouble())
    operator fun div(rhs: Float) = div(rhs.toDouble())

    operator fun plusAssign(rhs: Vector3) {
        x += rhs.x
        y += rhs.y
        z += rhs.z
    }
    operator fun minusAssign(rhs: Vector3) {
        x -= rhs.x
        y -= rhs.y
        z -= rhs.z
    }
    operator fun timesAssign(rhs: Double) {
        x *= rhs
        y *= rhs
        z *= rhs
    }
    operator fun divAssign(rhs: Double) {
        x /= rhs
        y /= rhs
        z /= rhs
    }

    operator fun timesAssign(rhs: Float) = timesAssign(rhs.toDouble())
    operator fun divAssign(rhs: Float) = divAssign(rhs.toDouble())

    infix fun dot(rhs: Vector3) = x * rhs.x + y * rhs.y + z * rhs.z
}