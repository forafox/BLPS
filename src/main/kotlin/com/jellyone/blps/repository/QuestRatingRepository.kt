package com.jellyone.blps.repository

import com.jellyone.blps.domain.QuestRating
import org.springframework.data.jpa.repository.JpaRepository

interface QuestRatingRepository : JpaRepository<QuestRating, Long> {
    fun findAllByQuestIdAndRelevanceIsTrue(questId: Long): List<QuestRating>
    fun countByBookingId(bookingId: Long): Long
    fun findByBookingId(bookingId: Long): List<QuestRating>
}