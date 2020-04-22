package ru.abbysoft.learnit.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@SpringBootApplication
class ServerApplication {
}

fun main(args: Array<String>) {
	runApplication<ServerApplication>(*args)
}
