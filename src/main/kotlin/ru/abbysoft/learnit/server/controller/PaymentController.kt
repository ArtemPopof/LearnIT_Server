package ru.abbysoft.learnit.server.controller

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import ru.abbysoft.learnit.server.data.UserDetailsImpl
import ru.abbysoft.learnit.server.util.JwtUtils
import ru.abbysoft.learnit.server.util.shopId
import ru.abbysoft.learnit.server.util.shopSecret
import java.nio.file.Files
import java.nio.file.Path
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

    private final fun readTemplate(): String {
        return Files.readString(Path.of(templatePath))
    }

    @PostMapping("purchase")
    @ResponseBody
    fun processPurchase(@RequestBody amount: Int): ResponseEntity<Any> {
        val userInfo = SecurityContextHolder.getContext().authentication.principal as String
        log.info("Payment request received from $userInfo with amount $amount")

        val request = fillTemplate(amount)
        val confirmationUrl = sendPaymentObject(request)

        return ResponseEntity.ok().body(confirmationUrl)
    }

    private fun fillTemplate(amount: Int): String {
       return template
                .replace("{0}", shopId)
                .replace("{1}", shopSecret)
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