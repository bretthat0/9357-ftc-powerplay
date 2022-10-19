package org.firstinspires.ftc.teamcode.util

enum class Direction {
    LEFT {
        override fun toVector2() = vec2(-1.0, 0.0)
    },
    RIGHT {
        override fun toVector2() = vec2(1.0, 0.0)
    },
    FORWARD {
        override fun toVector2() = vec2(0.0, 1.0)
    },
    BACKWARDS {
        override fun toVector2() = vec2(0.0, -1.0)
    };

    abstract fun toVector2(): Vector2
}