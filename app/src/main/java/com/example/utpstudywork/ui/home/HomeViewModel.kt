package com.example.utpstudywork.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.utpstudywork.data.di.UseCases
import com.example.utpstudywork.domain.model.Note
import com.example.utpstudywork.domain.model.SessionType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerUi(
    val type: SessionType = SessionType.WORK,
    val totalSec: Int = SessionType.WORK.defaultSeconds,
    val remainingSec: Int = SessionType.WORK.defaultSeconds,
    val running: Boolean = false
)

data class HomeUiState(
    val timer: TimerUi = TimerUi(),
    val workMinToday: Int = 0,
    val studyMinToday: Int = 0,
    val upcomingNotes: List<Note> = emptyList()
)

class HomeViewModel(private val use: UseCases) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state

    private var tickJob: Job? = null

    init {
        viewModelScope.launch { use.statsRepo.resetIfNewDay() }
        viewModelScope.launch {
            combine(
                use.statsRepo.observeToday(),
                use.getUpcomingNotes(System.currentTimeMillis())
            ) { stats, notes ->
                _state.value.copy(
                    workMinToday = stats.workMinutes,
                    studyMinToday = stats.studyMinutes,
                    upcomingNotes = notes.take(5)
                )
            }.collect { _state.value = it }
        }
    }

    fun switchTab(type: SessionType) {
        val total = type.defaultSeconds
        tickJob?.cancel()
        _state.update {
            it.copy(timer = it.timer.copy(type = type, totalSec = total, remainingSec = total, running = false))
        }
    }

    fun startTimer() {
        if (_state.value.timer.running) return
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            val t = _state.value.timer
            use.startTimer(t.type, t.remainingSec).collect { remaining ->
                _state.update { it.copy(timer = it.timer.copy(remainingSec = remaining, running = true)) }
                if (remaining <= 0) {
                    onFinishSession()
                    pauseTimer()
                }
            }
        }
    }

    fun pauseTimer() {
        use.pauseTimer()
        tickJob?.cancel()
        tickJob = null
        _state.update { it.copy(timer = it.timer.copy(running = false)) }
    }

    fun stopTimer() {
        use.stopTimer()
        tickJob?.cancel()
        tickJob = null
        val total = _state.value.timer.type.defaultSeconds
        _state.update { it.copy(timer = it.timer.copy(remainingSec = total, totalSec = total, running = false)) }
    }

    private fun onFinishSession() = viewModelScope.launch {
        val type = _state.value.timer.type
        val minutes = type.defaultMinutes
        use.statsRepo.addMinutes(
            workDelta = if (type == SessionType.WORK) minutes else 0,
            studyDelta = if (type == SessionType.STUDY) minutes else 0
        )
    }

    fun addNote(note: Note) = viewModelScope.launch { use.addNote(note) }
    fun deleteNote(id: String) = viewModelScope.launch { use.notesRepo?.delete(id) }
}
