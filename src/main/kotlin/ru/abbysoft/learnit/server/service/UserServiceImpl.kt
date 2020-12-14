package ru.abbysoft.learnit.server.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.MailSender
import org.springframework.mail.SimpleMailMessage
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import ru.abbysoft.learnit.server.exception.ServerException
import ru.abbysoft.learnit.server.exception.ValidationException
import ru.abbysoft.learnit.server.model.EUserRole
import ru.abbysoft.learnit.server.model.User
import ru.abbysoft.learnit.server.model.UserRole
import ru.abbysoft.learnit.server.repository.UserRepository
import ru.abbysoft.learnit.server.repository.UserRoleRepository
import ru.abbysoft.learnit.server.util.JwtUtils
import java.awt.SystemColor.text
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashSet
import org.springframework.mail.javamail.MimeMessageHelper
import javax.mail.internet.MimeMessage


@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    companion object {
        val log = LoggerFactory.getLogger(JwtUtils::class.java.name)
    }

    @Autowired
    private lateinit var encoder: PasswordEncoder
    @Autowired
    private lateinit var rolesRepository: UserRoleRepository
    @Autowired
    private lateinit var emailService: EmailService

    override fun confirmRegistration(secret: String, email: String) {
        val userInfo = userRepository.findByEmail(email).get()
        validateSecret(userInfo, secret)

        val newRoles = userInfo.roles.plus(rolesRepository.findByRole(EUserRole.BASIC).get())
        userInfo.roles = newRoles
        userInfo.confirmation = "confirmed"
        userRepository.save(userInfo)

        log.info("Account ${userInfo.name} confirmed")
    }

    private fun validateSecret(userInfo: User, secret: String) {
        if (secret != userInfo.confirmation) {
            log.error("Confirmation secret is wrong")
            throw ServerException("Confirmation secret $secret is wrong")
        }
    }

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
        val userEntity = User(-1L, user, email, encoder.encode(password), Timestamp.valueOf(LocalDateTime.now()), "", 3)

        userRepository.save(userEntity)

        emailService.sendConfirmationEmail(email, user, userEntity.confirmation)

        log.info("Registered new user $userEntity")
    }

    private fun userExist(user: String): Boolean {
        return userRepository.existsByName(user)
    }

    private fun emailOccupied(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    private fun passwordTooEasy(password: String): Boolean {
        if (password.length < 8) {
            return true
        }

        return false
    }

}