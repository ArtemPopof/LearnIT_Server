package ru.abbysoft.learnit.server.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.abbysoft.learnit.server.data.QuestionRepository
import ru.abbysoft.learnit.server.model.Question
import java.util.*

@RestController
@CrossOrigin(origins = ["http://localhost:10888"])
@RequestMapping("question")
class QuestionController {
    private val logger = LoggerFactory.getLogger(QuestionController::class.java)
    private val random = Random()

    @Autowired
    private lateinit var repository: QuestionRepository

    @GetMapping("random")
    fun getRandomQuestion() : Question {
        val questions = repository.findAll().toList()
        val randomIndex = random.nextInt(questions.size)
        return questions[randomIndex]
    }
}