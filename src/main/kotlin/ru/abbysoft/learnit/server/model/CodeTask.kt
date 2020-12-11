package ru.abbysoft.learnit.server.model

import javax.persistence.*

@Entity
data class CodeTask(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var title: String = "",
        var shortDescription: String = "",
        @Column(length = 2000)
        var fullDescription: String = "",
        var areas: String = "",
        var level: Int = -1
)