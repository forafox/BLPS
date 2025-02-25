package com.jellyone.blps.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.security.SecurityScheme


@OpenAPIDefinition(
    info = Info(
        title = "BLPS",
        description = "Sample API",
        version = "1.0.0",
    ),
    servers = [
        Server(url = "http://localhost:8080", description = "Default Server URL")
    ],
    tags = [
        Tag(name = "Authorization and Registration", description = "API for users"),
        Tag(name = "User Management", description = "API for users"),
        Tag(name = "Accommodations API", description = "API for accommodations"),
        Tag(name = "Bookings API", description = "API for bookings"),
        Tag(name = "Guests Ratings API", description = "API for guests"),
        Tag(name = "Accommodation Ratings API", description = "API for ratings")
    ]
)

@SecurityScheme(
    name = "JWT",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
class SwaggerConfig {

}