package ru.abbysoft.learnit.server.repository

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.abbysoft.learnit.server.model.Purchase
import ru.abbysoft.learnit.server.model.User

interface PurchaseDataRepository : CrudRepository<Purchase, Long> {

    @Query("SELECT purchase FROM Purchase purchase WHERE purchase.user = ?1 and purchase.validated = false")
    fun findNotValidatedByUser(user: User): Purchase?
}