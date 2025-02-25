package com.jellyone.blps.controller

import com.jellyone.blps.service.QuestRatingService
import com.jellyone.blps.web.request.CreateQuestRatingRequest
import com.jellyone.blps.web.request.UpdateQuestRatingRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quests")
@Tag(name = "Quests Ratings API")
@SecurityRequirement(name = "JWT")
class QuestRatingController(
    private val questRatingService: QuestRatingService
) {
    @PostMapping("/ratings")
    fun createQuestRating(
        @RequestBody response: CreateQuestRatingRequest
    ) = questRatingService.create(
        response.rating,
        response.feedback,
        response.date,
        response.relevance,
        response.questId,
        response.bookingId
    )

    @GetMapping("/ratings/{id}")
    fun getQuestRatingById(@PathVariable id: Long) =
        questRatingService.getById(id)

    @PutMapping("/ratings/{id}")
    fun updateQuestRatingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateQuestRatingRequest
    ) = questRatingService.update(
        id,
        response.rating,
        response.feedback,
        response.date,
        response.relevance
    )

    @DeleteMapping("/ratings/{id}")
    fun deleteQuestRatingById(@PathVariable id: Long) = questRatingService.delete(id)
}