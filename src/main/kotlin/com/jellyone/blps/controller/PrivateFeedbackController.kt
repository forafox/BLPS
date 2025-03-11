package com.jellyone.blps.controller

import com.jellyone.blps.service.PrivateFeedbackService
import com.jellyone.blps.web.response.toResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/private-feedbacks")
@Tag(name = "Private Feedback API")
@SecurityRequirement(name = "JWT")
class PrivateFeedbackController (
    private val privateFeedbackService: PrivateFeedbackService
){
    @GetMapping("/{id}")
    fun getPrivateFeedbackById(@PathVariable id: Long) =
        privateFeedbackService.getById(id).toResponse()

    @GetMapping("/owner/{ownerId}")
    fun getAllPrivateFeedbacksByOwnerId(@PathVariable ownerId: Long) =
        privateFeedbackService.getAllByOwnerId(ownerId).map { it.toResponse() }

}