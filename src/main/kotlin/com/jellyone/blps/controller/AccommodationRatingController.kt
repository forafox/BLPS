package com.jellyone.blps.controller

import com.jellyone.blps.service.AccommodationRatingService
import com.jellyone.blps.web.request.CreateAccommodationRatingRequest
import com.jellyone.blps.web.request.UpdateAccommodationRatingRequest
import com.jellyone.blps.web.response.toResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/accommodation-ratings")
@Tag(name = "Accommodation Ratings API")
@SecurityRequirement(name = "JWT")
class AccommodationRatingController(
    private val accommodationRatingService: AccommodationRatingService
) {
    @PostMapping("")
    fun createAccommodationRating(
        @RequestBody response: CreateAccommodationRatingRequest,
        principal: Principal
    ) = accommodationRatingService.create(
        response.overallImpression,
        response.purity,
        response.accuracy,
        response.arrival,
        response.communication,
        response.location,
        response.priceQuality,
        response.convenience,
        response.feedback,
        response.privateFeedback,
        response.date,
        response.accommodationId,
        response.bookingId,
        response.privateText,
        principal.name
    ).toResponse()

    @GetMapping("/{id}")
    fun getAccommodationRatingById(@PathVariable id: Long) =
        accommodationRatingService.getById(id).toResponse()

    @GetMapping("/accommodation/{accommodationId}")
    fun getAllAccommodationRatingsByAccommodationId(@PathVariable accommodationId: Long) =
        accommodationRatingService.getAllByAccommodationId(accommodationId).map { it.toResponse() }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    fun updateAccommodationRatingById(
        @PathVariable id: Long,
        @RequestBody response: UpdateAccommodationRatingRequest
    ) = accommodationRatingService.update(
        id,
        response.overallImpression,
        response.purity,
        response.accuracy,
        response.arrival,
        response.communication,
        response.location,
        response.priceQuality,
        response.convenience,
        response.feedback,
        response.date,
    ).toResponse()

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    fun deleteAccommodationRatingById(@PathVariable id: Long) = accommodationRatingService.delete(id)
}