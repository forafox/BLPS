package com.jellyone.blps.controller

import com.jellyone.blps.service.QuestRatingService
import com.jellyone.blps.web.request.CreateQuestRatingRequest
import com.jellyone.blps.web.request.UpdateQuestRatingRequest
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quests")
@Tag(name = "Guests Ratings API")
@SecurityRequirement(name = "JWT")
class GuestRatingController(
    private val questRatingService: QuestRatingService
) {
    @PostMapping("/ratings")
    fun createQuestRating(
        @RequestBody response: CreateQuestRatingRequest
    ) = questRatingService.create(
        response.rating,
        response.feedback,
        response.date,
        response.questId,
        response.bookingId
    )

    @GetMapping("/ratings/{id}")
    fun getQuestRatingById(@PathVariable id: Long) =
        questRatingService.getById(id)

    @GetMapping("/quest/{questId}")
    fun getAllQuestRatingsByQuestId(@PathVariable questId: Long) =
        questRatingService.getAllByQuestId(questId)

    @PutMapping("/ratings/{id}")
    fun updateQuestRatingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateQuestRatingRequest
    ) = questRatingService.update(
        id,
        response.rating,
        response.feedback,
        response.date,
    )

    @DeleteMapping("/ratings/{id}")
    fun deleteQuestRatingById(@PathVariable id: Long) = questRatingService.delete(id)
}