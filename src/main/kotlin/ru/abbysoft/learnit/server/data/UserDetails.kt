package ru.abbysoft.learnit.server.data

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.abbysoft.learnit.server.model.User
import java.util.stream.Collectors
import org.springframework.security.core.authority.SimpleGrantedAuthority



class UserDetailsImpl(val id: Long,
                      private val name: String,
                      @JsonIgnore
                      private val password: String,
                      private val email: String,
                      private val authorities: Collection<GrantedAuthority>) : UserDetails {

    companion object {
        fun build(info: User): UserDetailsImpl {
            val authorities = info.roles.stream()
                    .map { role -> SimpleGrantedAuthority(role.role.name) }
                    .collect(Collectors.toList())
            return UserDetailsImpl(info.id, info.name, info.password, info.email, authorities)
        }
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    fun getEmail(): String {
        return email
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun getUsername(): String {
        return name
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getPassword(): String {
        return password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

}