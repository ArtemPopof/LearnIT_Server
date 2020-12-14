package ru.abbysoft.learnit.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path

private const val CONFIRMATION_SUBJECT: String = "Подтверждение регистрации LearnIT"

@Service
class EmailService {

    private val templatePath: String = javaClass.getResource("/emailTemplate.html").path
    private val confirmationUrl: String = "http://localhost:10888/email_confirmation?secret=%s&email=%s"
    private val template: String = readTemplate()

    @Autowired
    private lateinit var emailSender: JavaMailSender

    private final fun readTemplate(): String {
        return Files.readString(Path.of(templatePath))
    }

    fun sendConfirmationEmail(email: String, user: String, confirmationSecret: String) {
        val mimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, "utf-8")
        helper.setFrom("contact@abbysoft.org")
        helper.setTo(email)
        helper.setSubject(CONFIRMATION_SUBJECT)
        helper.setText(getConfirmationEmailContent(user, confirmationSecret, email), true)

        emailSender.send(mimeMessage)

        UserServiceImpl.log.info("Email confirmation email sent to $email")
    }

    private fun getConfirmationEmailContent(user: String, secret: String, email: String): String {
        return template
                .replace("{\$\$\$0\$\$\$}", user)
                .replace("{\$\$\$1\$\$\$}", String.format(confirmationUrl, secret, email))
                .replace("{\$\$\$2\$\$\$}", String.format(confirmationUrl, secret, email))
    }
}