package com.jellyone.blps.service

import com.jellyone.blps.domain.QuestRating
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.QuestRatingRepository
import com.jellyone.blps.repository.RatingRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuestRatingService(
    private val questRatingRepository: QuestRatingRepository,
    private val userService: UserService,
    private val bookingService: BookingService,
    private val ratingRepository: RatingRepository
) {
    fun create(
        rating: Int,
        feedback: String,
        date: Date,
        questId: Long,
        bookingId: Long
    ): QuestRating {
        val questRating = QuestRating(
            id = 0,
            rating = rating,
            feedback = feedback,
            date = date,
            quest = userService.getById(questId),
            relevance = ratingRepository.hasMultipleRatingsForBooking(bookingId, true),
            booking = bookingService.getById(bookingId)
        )
        return questRatingRepository.save(questRating)
    }

    fun getById(id: Long): QuestRating {
        return questRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Quest rating not found") }
    }

    fun getAllByQuestId(questId: Long): List<QuestRating> {
        return questRatingRepository.findAllByQuestIdAndRelevanceIsTrue(questId)
    }

    fun update(
        id: Long,
        rating: Int,
        feedback: String,
        date: Date,
    ): QuestRating {
        val questRating = questRatingRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Quest rating not found") }
        return questRatingRepository.save(
            questRating.copy(
                rating = rating,
                feedback = feedback,
                date = date,
                relevance = false
            )
        )
    }

    fun delete(id: Long) {
        questRatingRepository.deleteById(id)
    }
}