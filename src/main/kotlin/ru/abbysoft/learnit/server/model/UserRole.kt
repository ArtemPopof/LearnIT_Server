package ru.abbysoft.learnit.server.model

import javax.persistence.*

@Entity
data class UserRole(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,

        @Enumerated(EnumType.STRING)
        @Column(length = 20)
        var role: EUserRole
)