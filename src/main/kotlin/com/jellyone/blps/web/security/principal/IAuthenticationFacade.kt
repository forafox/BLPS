package com.jellyone.blps.web.security.principal

import org.springframework.security.core.Authentication

interface IAuthenticationFacade {
    fun getAuthentication(): Authentication
    fun getAuthName(): String
    fun setAdminRole()
    fun isAdmin(): Boolean
}