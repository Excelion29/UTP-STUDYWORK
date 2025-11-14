package com.example.utpstudywork.domain.model

enum class SessionType(val defaultMinutes: Int) {
    WORK(25),
    STUDY(45);

    val defaultSeconds: Int get() = defaultMinutes * 60
}
