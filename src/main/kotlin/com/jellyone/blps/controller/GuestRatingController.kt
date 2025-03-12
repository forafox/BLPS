package com.jellyone.blps.controller

import com.jellyone.blps.service.GuestRatingService
import com.jellyone.blps.web.request.CreateGuestRatingRequest
import com.jellyone.blps.web.request.UpdateGuestRatingRequest
import com.jellyone.blps.web.response.toResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/guests")
@Tag(name = "Guests Ratings API")
@SecurityRequirement(name = "JWT")
class GuestRatingController(
    private val guestRatingService: GuestRatingService
) {
    @PostMapping("/ratings")
    fun createGuestRating(
        @RequestBody response: CreateGuestRatingRequest,
        principal: Principal
    ) = guestRatingService.create(
        response.rating,
        response.feedback,
        response.date,
        response.guestId,
        response.bookingId,
        principal.name
    ).toResponse()

    @GetMapping("/ratings/{id}")
    fun getGuestRatingById(@PathVariable id: Long) =
        guestRatingService.getById(id).toResponse()

    @GetMapping("/guest/{guestId}")
    fun getAllGuestRatingsByGuestId(@PathVariable guestId: Long) =
        guestRatingService.getAllByGuestId(guestId).map { it.toResponse() }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/ratings/{id}")
    fun updateGuestRatingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateGuestRatingRequest
    ) = guestRatingService.update(
        id,
        response.rating,
        response.feedback,
        response.date,
    ).toResponse()

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/ratings/{id}")
    fun deleteGuestRatingById(@PathVariable id: Long) = guestRatingService.delete(id)
}