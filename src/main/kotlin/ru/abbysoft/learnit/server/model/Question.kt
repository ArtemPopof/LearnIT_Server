package ru.abbysoft.learnit.server.model

import javax.persistence.*

@Entity
data class Question (
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        var id: Long = -1,

        var text: String = "",
        var answers: String = "",
        var rightAnswer: Int = -1
)