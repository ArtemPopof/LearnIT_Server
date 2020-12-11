package ru.abbysoft.learnit.server.exception

class ValidationException(override val message: String?, val field: String) : ServerException(message)