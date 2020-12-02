package ru.abbysoft.learnit.server.model

import java.sql.Timestamp
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class User(
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        var id: Long = -1,

        val name: String,
        val email: String,
        val password: String,
        val creationDate: Timestamp
)