package ru.abbysoft.learnit.server.util

import org.springframework.security.core.context.SecurityContextHolder

fun getUserNameFromAuthorization(): String {
    return SecurityContextHolder.getContext().authentication.principal as String
}