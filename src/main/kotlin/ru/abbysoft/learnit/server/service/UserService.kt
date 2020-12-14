package ru.abbysoft.learnit.server.service

import org.springframework.stereotype.Service

interface UserService {

    fun register(user: String, password: String, email: String)

    fun confirmRegistration(secret: String, email: String)
}