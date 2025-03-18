package com.jellyone.blps.web.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.kerberos.web.authentication.SpnegoAuthenticationProcessingFilter

class CustomSpnegoAuthenticationProcessingFilter : SpnegoAuthenticationProcessingFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            // Попытка аутентификации через Kerberos
            super.doFilter(request, response, chain)
        } catch (e: Exception) {
            // Если аутентификация не удалась, возвращаем 401
            response.status = HttpServletResponse.SC_UNAUTHORIZED
            response.writer.write("Kerberos authentication failed.")
            return
        }

        // Если аутентификация успешна, запрос не передается дальше
        if (SecurityContextHolder.getContext().authentication != null) {
            return
        }

        // Если аутентификация не удалась, запрос передается дальше
        chain.doFilter(request, response)
    }
}