package com.jellyone.blps.controller

import com.jellyone.blps.domain.enums.Role
import com.jellyone.blps.service.*
import com.jellyone.blps.web.response.GetMeResponse
import com.jellyone.blps.web.response.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api")
@Tag(name = "User Management")
@SecurityRequirement(name = "JWT")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/me")
    @Operation(summary = "Get current user", description = "Get current user")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "Current user successfully retrieved",
            content = [
                Content(
                    schema = Schema(implementation = GetMeResponse::class)
                )
            ]
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun me(principal: Principal): GetMeResponse {
        return GetMeResponse(0L, "test", Role.USER)
        val user = userService.getByUsername(principal.name)
        return GetMeResponse(user.id, principal.name, user.role)
    }

    @GetMapping("/users/{id}")
    @Operation(summary = "Get user by id", description = "Get user by id")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "User successfully retrieved",
            content = [Content(schema = Schema(implementation = GetMeResponse::class), mediaType = "application/json")]
        ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getUserById(@PathVariable id: Long): GetMeResponse {
        val user = userService.getById(id)
        return GetMeResponse(user.id, user.username, user.role)
    }

    @GetMapping("/users")
    @Operation(
        summary = "Get users with pagination and search",
        description = "Get paginated users with optional fuzzy search by fullName or username"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Users successfully retrieved",
                content = [Content(schema = Schema(implementation = Page::class), mediaType = "application/json")]
            ),
            ApiResponse(responseCode = "400", description = "Bad request"),
            ApiResponse(responseCode = "500", description = "Internal server error")
        ]
    )
    fun getUsers(
        @RequestParam(required = false, defaultValue = "") search: String,
        @RequestParam(required = false, defaultValue = "0") page: Int,
        @RequestParam(required = false, defaultValue = "10") size: Int,
        @RequestParam(required = false) productId: Long?,
        @RequestParam(required = false) teamId: Long?
    ): Page<UserResponse> {
        val users = userService.getUsers(search, productId, teamId, page, size)
        return users.map { user ->
            UserResponse(
                user.id,
                user.username,
                user.role,
                user.name,
                user.surname,
                user.email,
                user.phone
            )
        }
    }
}