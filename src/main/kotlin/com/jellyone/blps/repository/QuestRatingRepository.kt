package com.jellyone.blps.repository

import com.jellyone.blps.domain.QuestRating
import org.springframework.data.jpa.repository.JpaRepository

interface QuestRatingRepository : JpaRepository<QuestRating, Long> {
}