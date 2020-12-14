package ru.abbysoft.learnit.server.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.abbysoft.learnit.server.data.*
import ru.abbysoft.learnit.server.exception.ServerException
import ru.abbysoft.learnit.server.exception.ValidationException
import ru.abbysoft.learnit.server.repository.UserRepository
import ru.abbysoft.learnit.server.service.UserService
import ru.abbysoft.learnit.server.service.UserServiceImpl
import ru.abbysoft.learnit.server.util.JwtUtils
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("user")
@CrossOrigin(origins = ["http://2.57.184.76", "http://192.168.1.7", "http://localhost:10888", "https://abbysoft.org", "http://abbysoft.org"])
class UserController(@Autowired private val userService: UserService) {
    companion object {
        val log = LoggerFactory.getLogger(JwtUtils::class.java.name)
    }

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var authManager: AuthenticationManager
    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @PostMapping("login")
    @ResponseBody
    fun signin(request: HttpServletRequest, @RequestBody userInfo: LoginRequest): ResponseEntity<Any> {
        println("Login request received $request $userInfo")

        val authentication = authManager.authenticate(UsernamePasswordAuthenticationToken(userInfo.username, userInfo.password))
        SecurityContextHolder.getContext().authentication = authentication
        val jwtString = jwtUtils.generateJwtToken(authentication)

        val details = authentication.principal as UserDetailsImpl
        val fullInfo = userRepository.findByName(details.username).get()

        if (fullInfo.confirmation != "confirmed") {
            log.info("[ERROR] account not confirmed")
            return ResponseEntity.badRequest().body(ApiResponse("NOT_CONFIRMED", ""))
        }

        return ResponseEntity.ok(JwtResponse(jwtString, details.id, details.username, details.getEmail(), fullInfo.checks))
    }

    @PostMapping("confirm")
    @ResponseBody
    fun confirmRegistration(@RequestParam secret: String, @RequestParam email: String): ResponseEntity<Any> {
        userService.confirmRegistration(secret, email)

        return ResponseEntity.ok("confirmed")
    }

    @PostMapping("register")
    @ResponseBody
    fun register(request: HttpServletRequest, @RequestBody userInfo: UserInfo): ResponseEntity<ApiResponse> {
        println("Registration request received $request")

        try {
            validateParameters(userInfo.user, userInfo.password, userInfo.email)
            userService.register(userInfo.user, userInfo.password, userInfo.email as String)
        } catch (e : ValidationException) {
            return ResponseEntity.status(400).body(ApiResponse(e.message, e.field))
        }

        return ResponseEntity.ok(ApiResponse("", ""))
    }

    private fun validateParameters(user: String?, password: String?, email: String?) {
        if (user == null || user.isBlank()) {
            log.info("Blank data received: user ($user), password ($password), email ($email)")
            throw ValidationException("Имя пользователя не может быть пустым", "user")
        }
        if (email == null || email.isBlank()) {
            log.info("Blank data received: user ($user), password ($password), email ($email)")
            throw ValidationException("Email не может быть пустым", "email")
        }
        if (password == null || password.isBlank()) {
            log.info("Blank data received: user ($user), password ($password), email ($email)")
            throw ValidationException("Пароль не может быть пустым", "password")
        }
    }
}