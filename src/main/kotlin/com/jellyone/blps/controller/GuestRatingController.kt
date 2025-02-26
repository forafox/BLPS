package com.jellyone.blps.controller

import com.jellyone.blps.service.QuestRatingService
import com.jellyone.blps.web.request.CreateQuestRatingRequest
import com.jellyone.blps.web.request.UpdateQuestRatingRequest
import com.jellyone.blps.web.response.toResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/quests")
@Tag(name = "Guests Ratings API")
@SecurityRequirement(name = "JWT")
class GuestRatingController(
    private val questRatingService: QuestRatingService
) {
    @PostMapping("/ratings")
    fun createQuestRating(
        @RequestBody response: CreateQuestRatingRequest,
        principal: Principal
    ) = questRatingService.create(
        response.rating,
        response.feedback,
        response.date,
        response.questId,
        response.bookingId,
        principal.name
    ).toResponse()

    @GetMapping("/ratings/{id}")
    fun getQuestRatingById(@PathVariable id: Long) =
        questRatingService.getById(id).toResponse()

    @GetMapping("/quest/{questId}")
    fun getAllQuestRatingsByQuestId(@PathVariable questId: Long) =
        questRatingService.getAllByQuestId(questId).map { it.toResponse() }

    @PutMapping("/ratings/{id}")
    fun updateQuestRatingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateQuestRatingRequest
    ) = questRatingService.update(
        id,
        response.rating,
        response.feedback,
        response.date,
    ).toResponse()

    @DeleteMapping("/ratings/{id}")
    fun deleteQuestRatingById(@PathVariable id: Long) = questRatingService.delete(id)
}