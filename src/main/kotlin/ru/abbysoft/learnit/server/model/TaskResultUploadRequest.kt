package ru.abbysoft.learnit.server.model

data class TaskResultUploadRequest(
        val base64: String,
        val fileName: String,
        val taskId: Long
)
