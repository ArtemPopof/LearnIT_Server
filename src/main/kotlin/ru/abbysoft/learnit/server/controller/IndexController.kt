package ru.abbysoft.learnit.server.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class IndexController {

    @GetMapping("/")
    fun greetings() : String {
        return "<h1 style='color: red; font-size: 55px'>SERVER SIDE APP FOR LEARNIT APP</h1>"
    }
}