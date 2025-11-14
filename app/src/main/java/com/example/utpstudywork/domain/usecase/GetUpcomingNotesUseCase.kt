package com.example.utpstudywork.domain.usecase

import com.example.utpstudywork.domain.model.Note
import com.example.utpstudywork.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUpcomingNotesUseCase(private val repo: NotesRepository) {
    operator fun invoke(nowMillis: Long): Flow<List<Note>> =
        repo.observeNotes()
            .map { notes ->
                notes
                    .filter { it.triggerAtMillis >= nowMillis }
                    .sortedBy { it.triggerAtMillis }
            }
}
