package com.jellyone.blps.web.request

import com.jellyone.blps.domain.enums.Role

data class SignUpRequest(
    val username: String,
    val fullName: String,
    val password: String,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
    val role: Role
)