package org.firstinspires.ftc.teamcode.opmode.base

import kotlinx.coroutines.*
import kotlin.concurrent.thread

abstract class AutoBase: OpModeBase() {
    var isRunning: Boolean = false
        private set

    open suspend fun onStart() {}
    open fun onStop() {}

    override fun start() {
        thread {
            runBlocking {
                launch {
                    isRunning = true
                    onStart()
                }
            }
        }
    }

    override fun stop() {
        isRunning = false;
        onStop()
    }

    suspend fun wait(seconds: Double) {
        if (isRunning) {
            delay((seconds / 1000).toLong())
        }
    }

    suspend fun waitUntil(until: () -> Boolean) {
        while (!until() && isRunning) {
            delay(10)
        }
    }
}