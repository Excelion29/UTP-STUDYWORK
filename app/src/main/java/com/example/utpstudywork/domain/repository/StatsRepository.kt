package com.example.utpstudywork.domain.repository

import com.example.utpstudywork.domain.model.DailyStats
import kotlinx.coroutines.flow.Flow

interface StatsRepository {
    fun observeToday(): Flow<DailyStats>
    suspend fun addMinutes(workDelta: Int = 0, studyDelta: Int = 0)
    suspend fun resetIfNewDay()
}
