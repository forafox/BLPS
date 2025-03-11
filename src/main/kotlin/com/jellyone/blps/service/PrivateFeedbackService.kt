package com.jellyone.blps.service

import com.jellyone.blps.domain.PrivateFeedback
import com.jellyone.blps.exception.ResourceNotFoundException
import com.jellyone.blps.repository.PrivateFeedbackRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.*

@Service
class PrivateFeedbackService
    (
    private val privateFeedbackRepository: PrivateFeedbackRepository,
    private val userService: UserService,
    private val bookingService: BookingService
) {

    fun create(
        feedback: String,
        date: Date,
        bookingId: Long,
        username: String
    ): PrivateFeedback {
        val owner = bookingService.getOwnerByBookingId(bookingId)
        checkAbilityToFeedback(owner.id, username)
        val privateFeedback = PrivateFeedback(
            id = 0,
            feedback = feedback,
            date = date,
            owner = owner,
            booking = bookingService.getById(bookingId)
        )
        return privateFeedbackRepository.save(privateFeedback)
    }

    fun getById(id: Long): PrivateFeedback {
        return privateFeedbackRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Feedback not found") }
    }

    fun getAllByOwnerId(ownerId: Long): List<PrivateFeedback> {
        return privateFeedbackRepository.findAllByOwnerId(ownerId)
    }


    fun checkAbilityToFeedback(ownerId: Long, username: String) {
        val guest = userService.getById(ownerId)
        val evaluator = userService.getByUsername(username)
        if (guest.username == evaluator.username) {
            throw AccessDeniedException("You can't rate")
        }
    }
}