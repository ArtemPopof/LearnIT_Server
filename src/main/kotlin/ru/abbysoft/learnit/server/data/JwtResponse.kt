package ru.abbysoft.learnit.server.data

data class JwtResponse(
        val token: String,
        val id: Long,
        val username: String,
        val email: String
)