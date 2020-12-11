package ru.abbysoft.learnit.server.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.abbysoft.learnit.server.model.User
import java.util.*

interface UserRepository : CrudRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.name = ?1")
    fun findByName(user: String): Optional<User>

    @Query("SELECT user FROM User user WHERE user.email = ?1")
    fun findByEmail(email: String): User?

    fun existsByName(username: String): Boolean

    fun existsByEmail(email: String): Boolean?
}