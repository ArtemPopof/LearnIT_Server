package ru.abbysoft.learnit.server.filter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import ru.abbysoft.learnit.server.util.JwtUtils
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthTokenFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsService
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            val jwt = parseJwt(request)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                val username = jwtUtils.getUserNameFromJwtToken(jwt)

                val details = userDetailsService.loadUserByUsername(username)
                val authentication = UsernamePasswordAuthenticationToken(username, null, details.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            logger.error("Cannot set user authentication: {}", e)
        }

        chain.doFilter(request, response)
    }

    private fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7)
        }

        return null
    }
}