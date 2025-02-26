package com.jellyone.blps.repository

import com.jellyone.blps.domain.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    fun findByUsername(username: String): Optional<User>

    @Query(
        """
            SELECT u.* 
            FROM users u
            WHERE (:search IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) 
                   OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')))
            GROUP BY u.id
    """,
        nativeQuery = true
    )
    fun findAllUsersWithSomeParameters(
        @Param("search") search: String?,
        pageable: Pageable
    ): Page<User>
}