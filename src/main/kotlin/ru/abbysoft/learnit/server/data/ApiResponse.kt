package ru.abbysoft.learnit.server.data

val OK_RESPONSE: ApiResponse = ApiResponse("", "")

data class ApiResponse (
        val error: String?,
        val field: String?
)
