package com.jellyone.blps.service

import com.jellyone.blps.domain.User
import com.jellyone.blps.domain.enums.Role
import com.jellyone.blps.exception.ResourceAlreadyExistsException
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {

    fun register(
        username: String,
        password: String,
        name: String,
        surname: String,
        email: String,
        phone: String,
        role: Role
    ) {

        checkUserAlreadyExists(username)

        val user = User(
            id = 0,
            username = username,
            password = passwordEncoder.encode(password),
            name = name,
            surname = surname,
            email = email,
            phone = phone,
            role = role
        )
        userRepository.save(user)
    }

    fun getById(id: Long): User {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found") }
    }

    fun getByUsername(username: String): User {
        return userRepository.findByUsername(username)
            .orElseThrow { ResourceNotFoundException("User not found") }
    }

    fun getUsers(search: String, productId: Long?, teamId: Long?, page: Int, size: Int): Page<User> {
        val pageable: Pageable = PageRequest.of(page, size)
        return userRepository.findAllUsersWithSomeParameters(search, pageable)
    }

    private fun checkUserAlreadyExists(username: String) {
        if (userRepository.findByUsername(username).isPresent) {
            throw ResourceAlreadyExistsException("User already exists")
        }
    }

    private fun getUserSearchSpecification(search: String): Specification<User> {
        return Specification { root, _, criteriaBuilder ->
            if (search.isBlank()) {
                return@Specification null
            }
            val searchPattern = "%${search.lowercase()}%"
            criteriaBuilder.or(
                criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), searchPattern),
                criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), searchPattern)
            )
        }
    }
}