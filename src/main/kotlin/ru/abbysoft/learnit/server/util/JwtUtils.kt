package ru.abbysoft.learnit.server.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import ru.abbysoft.learnit.server.data.UserDetailsImpl
import java.util.*
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import org.slf4j.LoggerFactory
import java.security.SignatureException


@Component
class JwtUtils {
    companion object {
        val log = LoggerFactory.getLogger(JwtUtils::class.java.name)
    }

    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret: String
    @Value("\${app.jwtExpirrationMs}")
    private var jwtExpirationMs: Long = 1000000L

    fun generateJwtToken(auth: Authentication): String {
        val userPrincipal = auth.principal as UserDetailsImpl

        return Jwts.builder()
                .setSubject(userPrincipal.username)
                .setIssuedAt(Date())
                .setExpiration(Date(Date().time + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserNameFromJwtToken(token: String): String {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body.subject;
    }

    fun validateJwtToken(token: String): Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token)
            return true
        } catch (e: SignatureException) {
            log.error("Invalid JWT signature: {}", e.message)
        } catch (e: MalformedJwtException) {
            log.error("Invalid JWT token: {}", e.message)
        } catch (e: ExpiredJwtException) {
            log.error("JWT token is expired: {}", e.message)
        } catch (e: UnsupportedJwtException) {
            log.error("JWT token is unsupported: {}", e.message)
        } catch (e: IllegalArgumentException) {
            log.error("JWT claims string is empty: {}", e.message)
        }

        return false;
    }
}
