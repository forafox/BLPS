package com.jellyone.blps.controller

import com.jellyone.blps.domain.enums.Role
import com.jellyone.blps.web.response.GetMeResponse
import com.jellyone.blps.web.request.SignUpRequest
import com.jellyone.blps.web.request.JwtRequest
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
class UserControllerTest {

    @LocalServerPort
    private var port: Int = 0

    companion object {
        private var jwtToken: String? = null

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

    @Test
    fun getCurrentUserShouldReturnOk() {
        RestAssured.given()
            .auth().oauth2(jwtToken)
            .`when`()
            .get("/api/me")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(GetMeResponse::class.java)
    }

    @Test
    fun getUserByIdShouldReturnOk() {
        val userId = 1
        RestAssured.given()
            .auth().oauth2(jwtToken)
            .`when`()
            .get("/api/users/$userId")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
            .extract()
            .`as`(GetMeResponse::class.java)
    }

    @Test
    fun getUserByIdNotFoundShouldReturnNotFound() {
        val userId = 9999
        RestAssured.given()
            .auth().oauth2(jwtToken)
            .`when`()
            .get("/api/users/$userId")
            .then()
            .statusCode(HttpStatus.NOT_FOUND.value())
    }

    @Test
    fun getUsersWithPaginationShouldReturnOk() {
        RestAssured.given()
            .auth().oauth2(jwtToken)
            .queryParam("search", "test")
            .queryParam("page", 0)
            .queryParam("size", 10)
            .`when`()
            .get("/api/users")
            .then()
            .statusCode(HttpStatus.OK.value())
            .contentType(ContentType.JSON)
    }

    @Test
    fun getUsersWithoutAuthShouldReturnUnauthorized() {
        RestAssured.given()
            .`when`()
            .get("/api/users")
            .then()
            .statusCode(HttpStatus.UNAUTHORIZED.value())
    }

    fun registerTestUser() {
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
    }
}