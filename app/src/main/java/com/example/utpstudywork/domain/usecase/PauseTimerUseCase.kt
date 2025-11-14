package com.example.utpstudywork.domain.usecase
import com.example.utpstudywork.core.TimerEngine
class PauseTimerUseCase(private val engine: TimerEngine) { operator fun invoke() = engine.pause() }
