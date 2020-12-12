package ru.abbysoft.learnit.server.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.abbysoft.learnit.server.data.UserDetailsImpl
import ru.abbysoft.learnit.server.model.CodeTask
import ru.abbysoft.learnit.server.repository.CodeTasksRepository
import ru.abbysoft.learnit.server.repository.UserRepository

@RestController
@CrossOrigin(origins = ["http://2.57.184.76", "http://192.168.1.7", "http://localhost:10888", "https://abbysoft.org", "http://abbysoft.org"])
@RequestMapping("code")
class CodeTasksController {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(CodeTasksController::javaClass.name)
    }

    @Autowired
    private lateinit var codeTasksRepository: CodeTasksRepository
    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("availableTasks")
    @ResponseBody
    fun getAvailableTasks(@RequestParam level: Int) : ResponseEntity<Any> {
        val username = SecurityContextHolder.getContext().authentication.principal as String

        val userInfo = userRepository.findByName(username)
        val completeTasks = userInfo.get().completedTasks
        val allTasks = codeTasksRepository.findAll().filter { task -> task.level == level}.toHashSet()

        allTasks.removeAll(completeTasks)

        return ResponseEntity.ok(allTasks)
    }

    @PostMapping("save")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    fun save(@RequestBody codeTask: CodeTask): ResponseEntity<Any> {
        log.info("Saving task $codeTask")
        codeTasksRepository.save(codeTask)

        return ResponseEntity.ok("saved")
    }
}