package com.jellyone.blps.web.security

import com.jellyone.blps.domain.User
import com.jellyone.blps.domain.enums.Role
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class DummyUserDetailsService : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        // Здесь можно добавить логику для загрузки пользователя из базы данных
        return User(0L, "admin", "admin", "Admin", "admin", "admin@admin.com","+1234567890", Role.USER)
    }
}