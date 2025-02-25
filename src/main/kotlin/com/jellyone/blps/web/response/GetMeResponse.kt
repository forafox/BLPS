package com.jellyone.blps.web.response

import com.jellyone.blps.domain.enums.Role
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "DTO for read operations on the current user")
data class GetMeResponse(
    @Schema(description = "The ID of the user", example = "1")
    val id: Long,
    @Schema(description = "The username of the user", example = "j.doe")
    val username: String,
    @Schema(description = "The role of the user", example = "ADMIN")
    val role: Role,
)