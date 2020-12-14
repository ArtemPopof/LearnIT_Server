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
        var completedTasks: String,

        @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.PERSIST])
        @JoinTable(	name = "account_roles",
                joinColumns = [JoinColumn(name = "account_id")],
                inverseJoinColumns = [JoinColumn(name = "role_id")])
        var roles: Set<UserRole> = HashSet()
)