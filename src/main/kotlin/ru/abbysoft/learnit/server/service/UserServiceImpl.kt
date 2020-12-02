package ru.abbysoft.learnit.server.service

import org.springframework.stereotype.Service
import ru.abbysoft.learnit.server.exception.ServerException
import ru.abbysoft.learnit.server.model.User
import ru.abbysoft.learnit.server.repository.UserRepository
import java.sql.Timestamp
import java.time.LocalDateTime

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun register(user: String, password: String, email: String) {
        if (userExist(user)) {
            throw ServerException("Такой пользователь уже существует")
        }
        if (emailOccupied(email)) {
            throw ServerException("Пользователь с таким email уже существует")
        }
        if (passwordTooEasy(password)) {
            throw ServerException("Пароль слишком простой. Длина пароля должна быть 8 и более символов.")
        }

        registerNewUser(user, password, email);
    }

    private fun registerNewUser(user: String, password: String, email: String) {
        val userEntity = User(-1L, user, email, password, Timestamp.valueOf(LocalDateTime.now()))

        userRepository.save(userEntity)

        println("Registered new user $userEntity")
    }

    private fun userExist(user: String): Boolean {
        val userEntity = userRepository.findByName(user)
        return userEntity != null
    }

    private fun emailOccupied(email: String): Boolean {
        val userEntity = userRepository.findByEmail(email)
        return userEntity != null
    }

    private fun passwordTooEasy(password: String): Boolean {
        if (password.length < 8) {
            return true
        }

        return false
    }

}