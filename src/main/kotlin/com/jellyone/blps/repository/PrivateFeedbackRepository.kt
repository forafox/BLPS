package com.jellyone.blps.repository

import com.jellyone.blps.domain.PrivateFeedback
import org.springframework.data.jpa.repository.JpaRepository

interface PrivateFeedbackRepository : JpaRepository<PrivateFeedback, Long> {
    fun findAllByOwnerId(ownerId: Long) : List<PrivateFeedback>
}