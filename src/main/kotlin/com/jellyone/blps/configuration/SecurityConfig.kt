package com.jellyone.blps.configuration

import com.jellyone.blps.web.security.*
import com.jellyone.blps.web.security.principal.AuthenticationFacade
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationManagerResolver
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.kerberos.authentication.KerberosAuthenticationProvider
import org.springframework.security.kerberos.authentication.KerberosServiceAuthenticationProvider
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosClient
import org.springframework.security.kerberos.authentication.sun.SunJaasKerberosTicketValidator
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter
import org.springframework.security.kerberos.web.authentication.SpnegoEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    @Lazy @Autowired private val tokenProvider: JwtTokenProvider,
    @Lazy @Autowired private val authenticationFacade: AuthenticationFacade,
    @Lazy @Autowired private val jwtUsersDetailsService: JwtUsersDetailsService,
    @Lazy @Autowired private val authenticationConfiguration: AuthenticationConfiguration
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { configurer ->
                configurer.authenticationEntryPoint(spnegoEntryPoint())
                configurer.accessDeniedHandler { request, response, exception ->
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.writer.write("Forbidden.")
                }
            }
            .authorizeHttpRequests { configurer ->
                configurer
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                    .requestMatchers("/actuator", "/actuator/**", "/actuator/prometheus", "/**").permitAll()
                    .anyRequest().authenticated()
            }
            .anonymous { it.disable() }
            .addFilterBefore(JwtTokenFilter(tokenProvider, authenticationFacade), UsernamePasswordAuthenticationFilter::class.java)
            .addFilterBefore(CustomSpnegoAuthenticationProcessingFilter(kerberosAuthenticationManager()), BasicAuthenticationFilter::class.java)
        return http.build()
    }

    /**
     * Выбирает AuthenticationManager в зависимости от типа аутентификации (JWT / Kerberos)
     */
    @Bean
    fun authenticationManagerResolver(): AuthenticationManagerResolver<HttpServletRequest> {
        return AuthenticationManagerResolver { request ->
            if (request.getHeader(HttpHeaders.AUTHORIZATION)?.startsWith("Bearer ") == true) {
                jwtAuthenticationManager()
            } else {
                kerberosAuthenticationManager()
            }
        }
    }

    /**
     * Менеджер аутентификации для JWT
     */
    @Bean
    fun jwtAuthenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    /**
     * Менеджер аутентификации для Kerberos
     */
    @Bean
    fun kerberosAuthenticationManager(): AuthenticationManager {
        return ProviderManager(
            listOf(
                kerberosAuthenticationProvider(),
                kerberosServiceAuthenticationProvider()
            )
        )
    }

    @Bean
    fun kerberosAuthenticationProvider(): KerberosAuthenticationProvider {
        val provider = KerberosAuthenticationProvider()
        val client = SunJaasKerberosClient()
        client.setDebug(true)
        provider.setKerberosClient(client)
        provider.setUserDetailsService(jwtUsersDetailsService)
        return provider
    }

    @Bean
    fun spnegoAuthenticationProcessingFilter(@Qualifier("kerberosAuthenticationManager") authenticationManager: AuthenticationManager?): SpnegoAuthenticationProcessingFilter {
        val filter = SpnegoAuthenticationProcessingFilter()
        filter.setAuthenticationManager(authenticationManager)
        return filter
    }

    @Bean
    fun kerberosServiceAuthenticationProvider(): KerberosServiceAuthenticationProvider {
        val provider = KerberosServiceAuthenticationProvider()
        provider.setTicketValidator(sunJaasKerberosTicketValidator())
        provider.setUserDetailsService(jwtUsersDetailsService)
        return provider
    }

    @Bean
    fun spnegoEntryPoint(): SpnegoEntryPoint {
        return SpnegoEntryPoint("/login")
    }

    @Bean
    fun sunJaasKerberosTicketValidator(): SunJaasKerberosTicketValidator {
        val ticketValidator = SunJaasKerberosTicketValidator()
        ticketValidator.setServicePrincipal("HTTP/localhost@EXAMPLE.COM")
        ticketValidator.setKeyTabLocation(FileSystemResource("krb5.keytab"))
        ticketValidator.setDebug(true)
        return ticketValidator
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}