package com.example.utpstudywork.domain.usecase
import com.example.utpstudywork.domain.model.Note
import com.example.utpstudywork.domain.repository.NotesRepository

class AddNoteUseCase(private val repo: NotesRepository) {
    suspend operator fun invoke(note: Note) = repo.upsert(note)
}
