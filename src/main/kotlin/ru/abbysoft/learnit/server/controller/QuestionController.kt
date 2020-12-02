package ru.abbysoft.learnit.server.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import ru.abbysoft.learnit.server.data.AnswerRepository
import ru.abbysoft.learnit.server.data.QuestionRepository
import ru.abbysoft.learnit.server.model.Answer
import ru.abbysoft.learnit.server.model.Question
import ru.abbysoft.learnit.server.model.QuestionLevel
import java.lang.IllegalArgumentException
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList

private var numberInvoked = 0

@RestController
@CrossOrigin(origins = ["http://2.57.184.76", "http://192.168.1.7", "http://localhost:10888", "https://abbysoft.org", "http://abbysoft.org"])
@RequestMapping("question")
class QuestionController {
    private val logger = LoggerFactory.getLogger(QuestionController::class.java)
    private val random = Random()

    @Autowired
    private lateinit var questionRepository: QuestionRepository
    @Autowired
    private lateinit var answerRepository: AnswerRepository

    @PostMapping("answer")
    fun postAnswer(request: HttpServletRequest, @RequestParam question: Long, @RequestParam answer: String) {
        logger.info("Host ${request.remoteAddr} answered $answer to question $question")

        val answerEntity = Answer(host = request.remoteAddr, questionIndex = question, answer = answer)

        answerRepository.save(answerEntity)
    }

    @GetMapping("random")
    fun getRandomQuestions(request: HttpServletRequest, @RequestParam level: String, @RequestParam count: Int?) : Iterable<Question>? {
        logger.info("GetRandomQuestion (${numberInvoked++}) from ${request.remoteAddr}, Level ($level)")
        if (!checkArguments(level)) return null

        val questionCount = count ?: 1
        val questions = questionRepository.findAll().toList().filter { it.level.name.toLowerCase() == level.toLowerCase() }

        return chooseRandomQuestion(questions, questionCount)
    }

    private fun checkArguments(level: String): Boolean {
        return try {
            QuestionLevel.valueOf(level.toUpperCase())
            true
        } catch (e: IllegalArgumentException) {
            logger.error("Error parsing level: $level")
            false
        }
    }

    private fun chooseRandomQuestion(questions: Iterable<Question>, count: Int): Iterable<Question> {
        val questionSet = questions.toMutableSet()
        val chosenQuestions = ArrayList<Question>(count)

        for (i in 0 until count) {
            if (questionSet.isEmpty()) break
            chosenQuestions.add(questionSet.random())
            questionSet -= chosenQuestions[i]
        }

        logger.info("chosenQuestions size: ${chosenQuestions.size}")

        return chosenQuestions
    }

}