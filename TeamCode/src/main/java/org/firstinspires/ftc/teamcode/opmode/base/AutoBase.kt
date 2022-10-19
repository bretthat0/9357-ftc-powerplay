package org.firstinspires.ftc.teamcode.opmode.base

import kotlinx.coroutines.*

abstract class AutoBase: OpModeBase() {
    private lateinit var startJob: Job

    open suspend fun onStart() {}
    open fun onStop() {}

    override fun start() {
        runBlocking {
            startJob = launch {
                onStart()
            }
        }
    }

    override fun stop() {
        startJob.cancel()
        onStop()
    }

    suspend fun wait(seconds: Double) {
        delay((seconds / 1000).toLong())
    }

    suspend fun waitUntil(until: () -> Boolean) {
        while (!until()) {
            delay(10)
        }
    }
}