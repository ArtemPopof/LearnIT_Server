package ru.abbysoft.learnit.server.data

import org.springframework.data.repository.CrudRepository
import ru.abbysoft.learnit.server.model.Answer

interface AnswerRepository : CrudRepository<Answer, Long>