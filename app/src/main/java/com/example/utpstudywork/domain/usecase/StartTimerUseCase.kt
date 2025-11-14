package com.example.utpstudywork.domain.usecase

import com.example.utpstudywork.core.TimerEngine
import com.example.utpstudywork.domain.model.SessionType
import kotlinx.coroutines.flow.Flow

class StartTimerUseCase(private val engine: TimerEngine) {
    operator fun invoke(type: SessionType, totalSeconds: Int): Flow<Int> =
        engine.start(totalSeconds, tag = type.name)
}
