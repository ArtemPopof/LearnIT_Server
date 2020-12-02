package ru.abbysoft.learnit.server.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Answer(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var host: String = "",
        var questionIndex: Long = -1,
        var answer: String = ""
)