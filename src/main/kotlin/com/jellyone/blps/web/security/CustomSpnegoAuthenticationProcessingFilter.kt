package com.jellyone.blps.web.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter

class CustomSpnegoAuthenticationProcessingFilter(
    authenticationManager: AuthenticationManager
) : SpnegoAuthenticationProcessingFilter() {

    private val allowedPaths = arrayOf("docs", "swagger", "h2", "auth")

    init {
        this.setAuthenticationManager(authenticationManager)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        // Пропускаем запросы, которые не требуют аутентификации через Kerberos
        if (allowedPaths.any { request.requestURI.contains(it) }) {
            logger.info("Request to allowed path: ${request.requestURI}. Skipping Kerberos authentication.")
            chain.doFilter(request, response)
            return
        }

        // Проверяем наличие заголовка Authorization с префиксом Negotiate
        val authHeader = request.getHeader("Authorization")
        if (authHeader == null || !authHeader.startsWith("Negotiate ")) {
            logger.info("Request without Kerberos token. Passing it to the next filter.")
            chain.doFilter(request, response)
            return
        }

        try {
            // Попытка аутентификации через Kerberos
            super.doFilter(request, response, chain)
        } catch (e: Exception) {
            // Если аутентификация не удалась, возвращаем 401
            logger.error("Kerberos authentication failed: ${e.message}")
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Kerberos authentication failed.")
            return
        }

        // Если аутентификация успешна, запрос не передается дальше
        if (SecurityContextHolder.getContext().authentication != null) {
            logger.info("Kerberos authentication successful. Request will not be passed further.")
            return
        }

        // Если аутентификация не удалась, запрос передается дальше
        logger.info("Kerberos authentication failed. Passing request to the next filter.")
        chain.doFilter(request, response)
    }
}