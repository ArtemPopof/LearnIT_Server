package ru.abbysoft.learnit.server.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import ru.abbysoft.learnit.server.util.JwtUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthEntryPointJwt : AuthenticationEntryPoint {

    companion object {
        val log = LoggerFactory.getLogger(JwtUtils::class.java.name)
    }


    override fun commence(request: HttpServletRequest?, response: HttpServletResponse?, ex: AuthenticationException?) {
        log.error("Unauthorized error: {}", ex?.message)

        response?.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
    }
}