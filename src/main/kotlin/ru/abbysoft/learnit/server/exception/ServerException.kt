package ru.abbysoft.learnit.server.exception

data class ServerException(override val message: String?) : Exception(message)