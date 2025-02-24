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
            LEFT JOIN user_team utr ON utr.user_id = u.id AND utr.status = 'ACCEPTED'
            LEFT JOIN product_teams ptr ON ptr.team_id = utr.team_id AND ptr.status = 'ACCEPTED'
            WHERE (:search IS NULL OR LOWER(u.fullname) LIKE LOWER(CONCAT('%', :search, '%')) 
                   OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')))
              AND (:teamId IS NULL OR utr.team_id = :teamId)
              AND (:productId IS NULL OR ptr.product_id = :productId)
            GROUP BY u.id
    """,
        nativeQuery = true
    )
    fun findAllUsersWithSomeParameters(
        @Param("search") search: String?,
        @Param("productId") productId: Long?,
        @Param("teamId") teamId: Long?,
        pageable: Pageable
    ): Page<User>
}