package com.jellyone.blps.controller

import com.jellyone.blps.exception.ErrorMessage
import com.jellyone.blps.service.AuthService
import com.jellyone.blps.service.UserService
import com.jellyone.blps.web.request.SignInRequest
import com.jellyone.blps.web.request.SignUpRequest
import com.jellyone.blps.web.request.JwtRequest
import com.jellyone.blps.web.response.JwtResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import lombok.RequiredArgsConstructor
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerRequestBody

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
@Tag(name = "Authorization and Registration")
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "Successful operation",
        content = [Content(schema = Schema(implementation = JwtResponse::class), mediaType = "application/json")]
    ),
    ApiResponse(
        responseCode = "400",
        description = "Invalid input",
        content = [Content(schema = Schema(implementation = ErrorMessage::class), mediaType = "application/json")]
    )
)
class AuthController(
    private val authService: AuthService,
    private val userService: UserService
) {

    @PostMapping("/login")
    @Operation(
        summary = "User login",
        description = "Authenticates user based on provided credentials and generates JWT token",
        operationId = "login"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = [Content(schema = Schema(implementation = ErrorMessage::class), mediaType = "application/json")]
        )
    )
    fun login(@Valid @RequestBody loginRequest: SignInRequest): JwtResponse {
        return authService.login(JwtRequest(loginRequest.username, loginRequest.password))
    }

    @PostMapping("/register")
    @Operation(
        summary = "User registration",
        description = "Registers a new user with provided details and generates JWT token",
        operationId = "register"
    )
    @ApiResponses(
        ApiResponse(
            responseCode = "409",
            description = "",
            content = [Content(schema = Schema(implementation = ErrorMessage::class), mediaType = "application/json")]
        )
    )
    fun register(@Valid @RequestBody request: SignUpRequest): JwtResponse {
        userService.register(request.username, request.fullName, request.password)
        return authService.login(JwtRequest(request.username, request.password))
    }

    @PostMapping("/refresh")
    @Operation(
        summary = "Refresh token",
        description = "Refreshes JWT token based on provided refresh token",
        operationId = "refresh"
    )
    fun refresh(
        @RequestBody
        @Valid @SwaggerRequestBody(content = [Content(mediaType = "text/plain")]) @NotBlank
        @Parameter(description = "Refresh token used to generate a new JWT token", required = true) refreshToken: String
    ): JwtResponse {
        return authService.refresh(refreshToken)
    }
}