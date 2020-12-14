package ru.abbysoft.learnit.server.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.JavaMailSender
import ru.abbysoft.learnit.server.util.mailHost
import ru.abbysoft.learnit.server.util.mailPassword
import ru.abbysoft.learnit.server.util.mailPort
import ru.abbysoft.learnit.server.util.mailUser

@Configuration
class LearnitConfiguration {
    @Bean
    fun getJavaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = mailHost
        mailSender.port = mailPort

        mailSender.username = mailUser
        mailSender.password = mailPassword

        val props = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"

        return mailSender
    }
}