package com.jellyone.blps.service

import com.jellyone.blps.domain.User
import com.jellyone.blps.web.security.JwtTokenProvider
import com.jellyone.blps.web.request.JwtRequest
import com.jellyone.blps.web.response.JwtResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.AuthenticationManager

@Service
class AuthService @Autowired constructor(
    @Qualifier("jwtAuthenticationManager") private val authenticationManager: AuthenticationManager,
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun login(loginRequest: JwtRequest): JwtResponse {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    loginRequest.password
                )
            )
        } catch (e: Exception) {
            throw BadCredentialsException(e.message)
        }

        val user: User = userService.getByUsername(loginRequest.username)

        return JwtResponse(
            user.id,
            user.username,
            jwtTokenProvider.createAccessToken(user.id, user.username, user.role),
            jwtTokenProvider.createRefreshToken(user.id, user.username)
        )
    }

    fun refresh(refreshToken: String): JwtResponse {
        return jwtTokenProvider.refreshUserTokens(refreshToken)
    }
}