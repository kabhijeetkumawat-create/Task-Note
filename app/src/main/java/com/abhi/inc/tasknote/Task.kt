package com.abhi.inc.tasknote

import com.google.firebase.Timestamp

data class Task(
    var id: String = "",
    val title: String = "",
    val description: String = "",
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now()
)