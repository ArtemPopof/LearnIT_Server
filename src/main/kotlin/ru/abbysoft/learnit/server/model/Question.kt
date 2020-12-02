package ru.abbysoft.learnit.server.model

import javax.persistence.*

@Entity
data class Question (
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        var id: Long = -1,

        var text: String = "",
        @Column(length = 755)
        var answers: String = "",
        var rightAnswer: Int = -1,
        var level: QuestionLevel = QuestionLevel.JUNIOR
)

enum class QuestionLevel {
        JUNIOR,
        MIDDLE,
        SENIOR
}
