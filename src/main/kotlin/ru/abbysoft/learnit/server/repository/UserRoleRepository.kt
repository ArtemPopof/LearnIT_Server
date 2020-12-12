package ru.abbysoft.learnit.server.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.abbysoft.learnit.server.model.EUserRole
import ru.abbysoft.learnit.server.model.UserRole
import java.util.*

interface UserRoleRepository : JpaRepository<UserRole, Long> {
    fun findByRole(role: EUserRole): Optional<UserRole>
}