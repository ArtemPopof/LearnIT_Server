package ru.abbysoft.learnit.server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import ru.abbysoft.learnit.server.data.UserDetailsImpl
import ru.abbysoft.learnit.server.exception.ServerException
import ru.abbysoft.learnit.server.repository.UserRepository

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByName(username).orElseThrow {
            throw ServerException("User not found")
        }

        return UserDetailsImpl.build(user)
    }

}