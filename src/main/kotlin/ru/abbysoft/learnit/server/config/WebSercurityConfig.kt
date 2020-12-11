package ru.abbysoft.learnit.server.config


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import ru.abbysoft.learnit.server.filter.AuthTokenFilter
import ru.abbysoft.learnit.server.security.AuthEntryPointJwt


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
// securedEnabled = true,
// jsr250Enabled = true,
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var unauthorizedHandler: AuthEntryPointJwt

    @Bean
    fun authenticationJwtTokenFilter(): AuthTokenFilter {
        return AuthTokenFilter()
    }

    public override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder.userDetailsService<UserDetailsService>(userDetailsService).passwordEncoder(passwordEncoder())
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .authorizeRequests().antMatchers("/user/**").permitAll()
                .antMatchers("/cogde/**").permitAll().anyRequest().authenticated()

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}