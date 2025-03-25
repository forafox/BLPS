package com.jellyone.blps.service.quartz

import com.jellyone.blps.repository.AccommodationRatingRepository
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class UpdateRelevanceJob(
    private val ratingRepository: AccommodationRatingRepository
) : Job {

    override fun execute(context: JobExecutionContext) {
        println("UpdateRelevanceJob is running")
        val ratingsToUpdate = ratingRepository.findByRelevanceFalseAndDateBefore(
            Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14))
        )

        ratingsToUpdate.forEach { rating ->
            rating.relevance = true
            ratingRepository.save(rating)
        }
    }
}