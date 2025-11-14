package com.example.utpstudywork.domain.repository

import com.example.utpstudywork.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun observeNotes(): Flow<List<Note>>
    suspend fun upsert(note: Note)
    suspend fun delete(id: String)
}
