package com.example.utpstudywork.data.di

import android.content.Context
import com.example.utpstudywork.core.TimerEngine
import com.example.utpstudywork.data.repository.NotesRepositoryInMemory
import com.example.utpstudywork.data.repository.StatsRepositoryInMemory
import com.example.utpstudywork.domain.repository.NotesRepository
import com.example.utpstudywork.domain.repository.StatsRepository
import com.example.utpstudywork.domain.usecase.*

data class UseCases(
    val startTimer: StartTimerUseCase,
    val pauseTimer: PauseTimerUseCase,
    val stopTimer: StopTimerUseCase,
    val addNote: AddNoteUseCase,
    val getUpcomingNotes: GetUpcomingNotesUseCase,
    val statsRepo: StatsRepository,
    val notesRepo: NotesRepository
)

object ServiceLocator {
    fun provideUseCases(context: Context): UseCases {
        val timer = TimerEngine()

        val statsRepo: StatsRepository = StatsRepositoryInMemory()
        val notesRepo: NotesRepository = NotesRepositoryInMemory()

        return UseCases(
            startTimer = StartTimerUseCase(timer),
            pauseTimer = PauseTimerUseCase(timer),
            stopTimer  = StopTimerUseCase(timer),
            addNote = AddNoteUseCase(notesRepo),
            getUpcomingNotes = GetUpcomingNotesUseCase(notesRepo),
            statsRepo = statsRepo,
            notesRepo = notesRepo
        )
    }
}
