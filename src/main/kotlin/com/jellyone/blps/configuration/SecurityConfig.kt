package com.jellyone.blps.configuration

//import com.jellyone.blps.web.security.DummyUserDetailsService

//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//class SecurityConfig(
//    @Lazy private val tokenProvider: JwtTokenProvider,
//    private val applicationContext: ApplicationContext,
//    private val requestIdFilter: RequestIdFilter,
//    private val authenticationFacade: AuthenticationFacade,
////    private val dummyUserDetailsService: DummyUserDetailsService
//    @Lazy private val jwtUsersDetailsService: JwtUsersDetailsService
//) {
//
//    @Bean
//    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
//        httpSecurity
//            .csrf { it.disable() }
//            .cors { it.disable() }
//            .httpBasic { it.disable() }
//            .sessionManagement { sessionManagement ->
//                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            }
//            .exceptionHandling { configurer ->
//                configurer.authenticationEntryPoint(spnegoEntryPoint())
//                configurer.accessDeniedHandler { request, response, exception ->
//                    response.status = HttpStatus.FORBIDDEN.value()
//                    response.writer.write("Forbidden.")
//                }
//            }
//            .authorizeHttpRequests { configurer ->
//                configurer
//                    .requestMatchers("/api/auth/**").permitAll()
//                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
//                    .requestMatchers("/actuator", "/actuator/**", "/actuator/prometheus", "/**").permitAll()
//                    .anyRequest().authenticated()
//            }
//            .anonymous { it.disable() }
//            .addFilterBefore(CustomSpnegoAuthenticationProcessingFilter(), BasicAuthenticationFilter::class.java)
////            .addFilterBefore(requestIdFilter, UsernamePasswordAuthenticationFilter::class.java)
////            .addFilterBefore(
////                JwtTokenFilter(tokenProvider, authenticationFacade),
////                UsernamePasswordAuthenticationFilter::class.java
////            )
//        return httpSecurity.build()
//    }
//
//    @Bean
//    fun passwordEncoder(): PasswordEncoder {
//        return BCryptPasswordEncoder()
//    }
//
////    @Bean
////    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
////        return configuration.authenticationManager
////    }
//
//    @Bean
//    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
//        return configuration.authenticationManager
//    }
//
//    @Bean
//    fun expressionHandler(): MethodSecurityExpressionHandler {
//        val expressionHandler = CustomSecurityExpressionHandler()
//        expressionHandler.setApplicationContext(applicationContext)
//        return expressionHandler
//    }
//
//    @Throws(Exception::class)
//    protected fun configure(auth: AuthenticationManagerBuilder) {
//        auth
//            .authenticationProvider(kerberosAuthenticationProvider())
//    }
//
//    @Bean
//    fun kerberosAuthenticationProvider(): KerberosAuthenticationProvider {
//        val provider = KerberosAuthenticationProvider()
//        val client = SunJaasKerberosClient()
//        client.setDebug(true)
//        provider.setKerberosClient(client)
//        provider.setUserDetailsService(jwtUsersDetailsService)
//        return provider
//    }
//
//    @Bean
//    fun spnegoEntryPoint(): SpnegoEntryPoint {
//        return SpnegoEntryPoint()
//    }
//
//    @Bean
//    fun spnegoAuthenticationProcessingFilter(): SpnegoAuthenticationProcessingFilter {
//        val filter = SpnegoAuthenticationProcessingFilter()
//        filter.setAuthenticationManager(authenticationManager(applicationContext.getBean(AuthenticationConfiguration::class.java)))
//        return filter
//    }
//}


import com.jellyone.blps.web.security.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class WebSecurityConfig() {

    @Autowired
    private lateinit var authenticationConfiguration: AuthenticationConfiguration


    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint(spnegoEntryPoint()) } // Добавляем SpnegoEntryPoint
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .addFilterBefore(spnegoAuthenticationProcessingFilter(authenticationManager()), BasicAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
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
        provider.setUserDetailsService(dummyUserDetailsService())
        return provider
    }

    @Bean
    fun spnegoAuthenticationProcessingFilter(authenticationManager: AuthenticationManager?): SpnegoAuthenticationProcessingFilter {
        val filter = SpnegoAuthenticationProcessingFilter()
        filter.setAuthenticationManager(authenticationManager)
        return filter
    }

    @Bean
    fun kerberosServiceAuthenticationProvider(): KerberosServiceAuthenticationProvider {
        val provider = KerberosServiceAuthenticationProvider()
        provider.setTicketValidator(sunJaasKerberosTicketValidator())
        provider.setUserDetailsService(dummyUserDetailsService())
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
    fun dummyUserDetailsService(): DummyUserDetailsService {
        return DummyUserDetailsService()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}