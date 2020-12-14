package ru.abbysoft.learnit.server.exception

open class ServerException(override val message: String?) : Exception(message)