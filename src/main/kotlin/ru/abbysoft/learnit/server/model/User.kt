package ru.abbysoft.learnit.server.model

import java.sql.Timestamp
import javax.persistence.*

@Entity
@Table(name = "account")
data class User(
        @Id
        @GeneratedValue(strategy= GenerationType.AUTO)
        var id: Long = -1,

        var name: String,
        var email: String,
        var password: String,
        var creationDate: Timestamp,
        @OneToMany(fetch = FetchType.LAZY)
        @JoinColumn(name = "code_task_id")
        var completedTasks: Set<CodeTask>
)