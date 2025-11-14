package com.example.utpstudywork.data.repository

import com.example.utpstudywork.domain.model.DailyStats
import com.example.utpstudywork.domain.repository.StatsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

class StatsRepositoryInMemory : StatsRepository {
    private val todayEpoch = TimeUnit.DAYS.toMillis(System.currentTimeMillis() / TimeUnit.DAYS.toMillis(1))
    private val state = MutableStateFlow(DailyStats(0, 0, todayEpoch))

    override fun observeToday() = state.asStateFlow()

    override suspend fun addMinutes(workDelta: Int, studyDelta: Int) {
        state.update {
            it.copy(
                workMinutes = (it.workMinutes + workDelta).coerceAtLeast(0),
                studyMinutes = (it.studyMinutes + studyDelta).coerceAtLeast(0)
            )
        }
    }

    override suspend fun resetIfNewDay() {
        val nowDay = TimeUnit.DAYS.toMillis(System.currentTimeMillis() / TimeUnit.DAYS.toMillis(1))
        if (state.value.dayEpoch != nowDay) state.value = DailyStats(0, 0, nowDay)
    }
}
