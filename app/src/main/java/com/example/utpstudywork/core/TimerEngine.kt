package com.example.utpstudywork.core

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TimerEngine {
    private var job: Job? = null
    private var remaining = 0
    private val _ticks = MutableSharedFlow<Int>(replay = 1)
    val ticks: SharedFlow<Int> = _ticks

    fun start(totalSeconds: Int, tag: String = ""): SharedFlow<Int> {
        stop()
        remaining = totalSeconds
        job = CoroutineScope(Dispatchers.Default).launch {
            while (remaining >= 0 && isActive) {
                _ticks.emit(remaining)
                delay(1000)
                remaining--
            }
        }
        return ticks
    }

    fun pause() { job?.cancel(); job = null }
    fun stop() { job?.cancel(); job = null; remaining = 0 }
}
