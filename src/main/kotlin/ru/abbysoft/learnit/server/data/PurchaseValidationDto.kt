package ru.abbysoft.learnit.server.data

data class PurchaseValidationDto(
        val operationid: String,
        val notificationType: String,
        val dateTime: String,
        val sha1Hash: String,
        val sender: String,
        val codepro: Boolean,
        val currency: Int,
        val amount: Float,
        val withdrawAmount: Float,
        val label: String,
        val lastName: String,
        val firstName: String,
        val fathersname: String,
        val zip: String,
        val city: String,
        val street: String,
        val building: String,
        val suite: String,
        val flat: String,
        val phone: String,
        val email: String
)