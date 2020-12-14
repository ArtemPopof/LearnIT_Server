package ru.abbysoft.learnit.server.controller

import org.postgresql.util.Base64
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ru.abbysoft.learnit.server.data.UserDetailsImpl
import ru.abbysoft.learnit.server.model.CodeTask
import ru.abbysoft.learnit.server.model.TaskResultUploadRequest
import ru.abbysoft.learnit.server.repository.CodeTasksRepository
import ru.abbysoft.learnit.server.repository.UserRepository
import ru.abbysoft.learnit.server.util.getUserNameFromAuthorization
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

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

    @Value(value = "\${installdir}")
    private lateinit var installPath: String

    @GetMapping("availableTasks")
    @ResponseBody
    fun getAvailableTasks(@RequestParam level: Int) : ResponseEntity<Any> {
        val username = SecurityContextHolder.getContext().authentication.principal as String

        val userInfo = userRepository.findByName(username)
        val completeTasks = userInfo.get().completedTasks
        val allTasks = codeTasksRepository.findAll().filter { task -> task.level == level}.toHashSet()

        val filtered = allTasks.filter {
            !completeTasks.split(";").contains("" + it.id)
        }

        return ResponseEntity.ok(filtered)
    }

    @PostMapping("save")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN')")
    fun save(@RequestBody codeTask: CodeTask): ResponseEntity<Any> {
        log.info("Saving task $codeTask")
        codeTasksRepository.save(codeTask)

        return ResponseEntity.ok("saved")
    }

    @PostMapping("submit")
    @PreAuthorize("hasAuthority('BASIC')")
    @ResponseBody
    fun submitTask(@RequestBody request: TaskResultUploadRequest): ResponseEntity<Any> {
        log.info("Submiting task result ${request.fileName}")

        val trimmed = request.base64.split("base64")[1].substring(1)
        val decoded = Base64.decode(trimmed)

        val userName = getUserNameFromAuthorization()
        val file = File(installPath + "/tasks/$userName/${request.taskId}" + "__" + request.fileName)
        file.parentFile.mkdirs()
        file.createNewFile()

        FileOutputStream(file).use {
            it.write(decoded)
        }

        val user = userRepository.findByName(userName)
        user.get().completedTasks = user.get().completedTasks + ";" + request.taskId
        userRepository.save(user.get())

        return ResponseEntity.ok("saved")
    }

    private fun createTasksDirIfNotExist() {
        val file = File("tasks/")
        if (file.exists()) {
            return
        }

        file.createNewFile()
    }
}