package ru.abbysoft.learnit.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.abbysoft.learnit.server.model.CodeTask

interface CodeTasksRepository : JpaRepository<CodeTask, Long>