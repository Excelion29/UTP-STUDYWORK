package com.example.utpstudywork.data.repository

import com.example.utpstudywork.domain.model.Note
import com.example.utpstudywork.domain.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesRepositoryInMemory : NotesRepository {
    private val notes = MutableStateFlow<List<Note>>(emptyList())
    override fun observeNotes() = notes.asStateFlow()
    override suspend fun upsert(note: Note) {
        notes.update { list -> list.filter { it.id != note.id } + note }
    }
    override suspend fun delete(id: String) { notes.update { it.filterNot { n -> n.id == id } } }
}
