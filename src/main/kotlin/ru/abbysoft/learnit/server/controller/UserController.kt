package ru.abbysoft.learnit.server.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.abbysoft.learnit.server.exception.ServerException
import ru.abbysoft.learnit.server.service.UserService
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("user")
class UserController(@Autowired private val userService: UserService) {

    @GetMapping("register")
    fun register(request: HttpServletRequest, @RequestParam user: String,
                 @RequestParam password: String, @RequestParam email: String) {
        println("Registration request received $request")
        validateParameters(user, password, email);
        userService.register(user, password, email);
    }

    private fun validateParameters(user: String, password: String, email: String) {
        if (user.isBlank() || password.isBlank() || email.isBlank()) {
            // TODO replace with logger
            println("Blank data received: user ($user), password ($password), email ($email)")
            throw ServerException("Одно из обязательных полей пусто")
        }
    }
}