package ru.abbysoft.learnit.server.controller

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.abbysoft.learnit.server.data.PurchaseData
import ru.abbysoft.learnit.server.data.PurchaseValidationDto
import ru.abbysoft.learnit.server.model.Purchase
import ru.abbysoft.learnit.server.model.User
import ru.abbysoft.learnit.server.repository.PurchaseDataRepository
import ru.abbysoft.learnit.server.repository.UserRepository
import ru.abbysoft.learnit.server.util.JwtUtils
import java.nio.file.Files
import java.nio.file.Path
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*

@RestController
@RequestMapping("payment")
@CrossOrigin(origins = ["http://2.57.184.76", "http://192.168.1.7", "http://localhost:10888", "https://abbysoft.org", "http://abbysoft.org"])
class PaymentController {
    companion object {
        val log = LoggerFactory.getLogger(JwtUtils::class.java.name)
    }

    private val templatePath: String = javaClass.getResource("/paymentTemplate").path
    private val template: String = readTemplate()

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var purchaseRepository: PurchaseDataRepository

    private final fun readTemplate(): String {
        return Files.readString(Path.of(templatePath))
    }

    @PostMapping("validate", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @ResponseBody
    fun validatePayment(validationData: PurchaseValidationDto) {
        log.info("Payment validation data received: $validationData")
        if (userRepository.existsByEmail(validationData.email)) {
            proceedValidationWithUser(validationData, userRepository.findByEmail(validationData.email).get())
        } else {
            proceedValidationWithoutUser(validationData)
        }
    }

    private fun proceedValidationWithoutUser(validationData: PurchaseValidationDto) {
        log.warn("Can't find user based on provided email")
        log.warn("Payment may fail")
    }

    private fun proceedValidationWithUser(validationData: PurchaseValidationDto, user: User) {
        val purchases = user.
    }

    @PostMapping("purchase")
    @ResponseBody
    fun processPurchase(@RequestBody purchaseData: PurchaseData): ResponseEntity<Any> {
        val username = SecurityContextHolder.getContext().authentication.principal as String
        log.info("Payment request received from $username with amount ${purchaseData.sum}")

        val userInfo = userRepository.findByName(username).get()

        val purchaseEntity = Purchase(-1L,
                purchaseData.checks,
                purchaseData.sum,
                Timestamp.valueOf(LocalDateTime.now()), false, userInfo)
        purchaseRepository.save(purchaseEntity)

        log.info("Payment registered successfully")

        return ResponseEntity.ok().body("Ok")
    }

    private fun fillTemplate(amount: Int): String {
       return template
                //.replace("{0}", shopId)
                //.replace("{1}", shopSecret)
                .replace("{2}", UUID.randomUUID().toString())
                .replace("{3}", "" + amount)
    }

    private fun sendPaymentObject(request: String): String {
        log.debug("sending payment request: $request")

        val processed = request.replace("\n", " ")

        val process = Runtime.getRuntime().exec(processed)
        process.waitFor()
        val response = String(process.inputStream.readAllBytes())
        val error = String(process.errorStream.readAllBytes())

        log.info("payment request error: $error")
        log.info("payment request response: $response")

        return "google"
    }

}