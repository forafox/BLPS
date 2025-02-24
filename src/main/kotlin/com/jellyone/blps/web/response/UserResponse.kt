package com.jellyone.blps.web.response

import com.jellyone.blps.domain.User
import com.jellyone.blps.domain.enums.Role

data class UserResponse(
    val id: Long,
    val username: String,
    val fullName: String,
    val role: Role
)

fun User.toResponse() = UserResponse(
    id = id,
    username = username,
    fullName = fullName,
    role = role
)