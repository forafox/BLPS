package com.jellyone.blps.web.response

import com.jellyone.blps.domain.User
import com.jellyone.blps.domain.enums.Role

data class UserResponse(
    val id: Long,
    val username: String,
    val role: Role,
    val name: String,
    val surname: String,
    val email: String,
    val phone: String,
)

fun User.toResponse() = UserResponse(
    id = id,
    username = username,
    role = role,
    name = name,
    surname = surname,
    email = email,
    phone = phone,
)