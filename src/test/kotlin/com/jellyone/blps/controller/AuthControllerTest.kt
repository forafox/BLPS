package com.jellyone.blps.controller

import com.jellyone.blps.domain.enums.Role
import com.jellyone.blps.web.request.JwtRequest
import com.jellyone.blps.web.request.SignUpRequest
import com.jellyone.blps.web.response.JwtResponse
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureMockMvc
class AuthControllerTest {

    @LocalServerPort
    private var port: Int = 0

    companion object {

        private var jwtToken: String? = null
        private var refreshToken: String? = null

        @Container
        private val postgres = PostgreSQLContainer<Nothing>("postgres:16-alpine")

        @DynamicPropertySource
        @JvmStatic
        fun configureProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
        }
    }

    @BeforeEach
    fun setUp() {
        RestAssured.baseURI = "http://localhost:$port"
        if (jwtToken == null) {
            registerTestUser()
            loginUser()
        }
    }

    private fun registerTestUser() {
        val signUpRequest =
            SignUpRequest(
                username = "testuser",
                password = "password",
                name = "Test User",
                surname = "User",
                email = "test@test.com",
                phone = "123456789",
                role = Role.USER
            )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(signUpRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/register")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    private fun loginUser() {
        val loginRequest = JwtRequest(username = "testuser", password = "password")
        val response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/login")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`(JwtResponse::class.java)

        jwtToken = response.accessToken
        refreshToken = response.refreshToken
    }

    @Test
    fun refreshNoJwtTokenShouldReturnBadRequest() {
        RestAssured.given()
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }

    @Test
    fun refreshBadJwtTokenShouldReturnUnauthorized() {
        val invalidToken =
            "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdXBlcm1hbkBnbWFpbC5jb20iLCJpZCI6MSwicm9sZXMiOlsiQURNSU4iXSwiaWF0IjoxNzE2MzA5NjExLCJleHAiOjE3MTYzMTMyMTF9.INVALID_TOKEN"

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(invalidToken)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun refreshWithoutAccessTokenWithBadRefreshTokenShouldReturnUnauthorized() {
        val badRefreshToken = "eyJhbGciOiJIUzUxMiJ9.INVALID_REFRESH_TOKEN"

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(badRefreshToken)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun refreshWithoutAccessTokenWithGoodRefreshTokenShouldReturnOk() {
        val response = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(refreshToken)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .`as`(JwtResponse::class.java)

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(response.refreshToken)
            .`when`()
            .post("/api/auth/refresh")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun registerShouldBeAvailableWithoutJWT() {
        val signUpRequest =
            SignUpRequest(
                username = "testuser2",
                password = "password",
                name = "Test User",
                surname = "User",
                email = "test@test.com",
                phone = "123456789",
                role = Role.USER
            )
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(signUpRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/register")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun loginWithIncorrectCredentialsShouldReturnUnauthorized() {
        val loginRequest = JwtRequest(username = "wronguser", password = "wrongpassword")

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/login")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    @Test
    fun loginWithCorrectCredentialsShouldReturnOk() {
        val loginRequest = JwtRequest(username = "testuser", password = "password")

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(loginRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/login")
            .then()
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun registerWithExistingUsernameShouldReturnConflict() {
        val signUpRequest =
            SignUpRequest(
                username = "testuser",
                password = "password",
                name = "Test User",
                surname = "User",
                email = "test@test.com",
                phone = "123456789",
                role = Role.USER
            )

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(signUpRequest)
            .accept(ContentType.JSON)
            .`when`()
            .post("/api/auth/register")
            .then()
            .statusCode(HttpStatus.CONFLICT.value())
    }
}