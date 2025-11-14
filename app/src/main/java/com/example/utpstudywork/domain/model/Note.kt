package com.example.utpstudywork.domain.model

data class Note(
    val id: String,
    val title: String,
    val description: String,
    val triggerAtMillis: Long
)
