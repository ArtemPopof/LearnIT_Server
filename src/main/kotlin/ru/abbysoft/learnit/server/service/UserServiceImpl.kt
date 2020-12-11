package ru.abbysoft.learnit.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.abbysoft.learnit.server.exception.ServerException
import ru.abbysoft.learnit.server.exception.ValidationException
import ru.abbysoft.learnit.server.model.User
import ru.abbysoft.learnit.server.repository.UserRepository
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    @Autowired
    private lateinit var encoder: PasswordEncoder

    override fun register(user: String, password: String, email: String) {
        if (userExist(user)) {
            throw ValidationException("Такой пользователь уже существует", "user")
        }
        if (emailOccupied(email)) {
            throw ValidationException("Пользователь с таким email уже существует", "email")
        }
        if (passwordTooEasy(password)) {
            throw ValidationException("Пароль слишком простой. Длина пароля должна быть 8 и более символов", "password")
        }

        registerNewUser(user, password, email);
    }

    private fun registerNewUser(user: String, password: String, email: String) {
        val userEntity = User(-1L, user, email, encoder.encode(password), Timestamp.valueOf(LocalDateTime.now()), Collections.emptySet())

        userRepository.save(userEntity)

        println("Registered new user $userEntity")
    }

    private fun userExist(user: String): Boolean {
        return userRepository.existsByName(user)
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