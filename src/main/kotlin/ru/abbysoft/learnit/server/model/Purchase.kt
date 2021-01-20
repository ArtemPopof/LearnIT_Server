package ru.abbysoft.learnit.server.model

import java.sql.Timestamp
import javax.persistence.*

@Entity
data class Purchase(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = -1,
        var checks: Int = -1,
        var sum: Int = -1,
        var updateTimestamp: Timestamp? = null,
        var validated: Boolean = false,
        @ManyToOne
        var user: User
)