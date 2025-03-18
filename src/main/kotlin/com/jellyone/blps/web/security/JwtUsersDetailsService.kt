package com.jellyone.blps.web.security

import com.jellyone.blps.domain.User
import com.jellyone.blps.service.UserService
import lombok.RequiredArgsConstructor
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

//@Service
//@RequiredArgsConstructor
//class JwtUsersDetailsService(private val userService: UserService) : UserDetailsService {
//
//    @Throws(UsernameNotFoundException::class)
//    override fun loadUserByUsername(username: String): UserDetails {
//        val user: User = userService.getByUsername(username)
//        return JwtEntityFactory.create(user)
//    }
//}