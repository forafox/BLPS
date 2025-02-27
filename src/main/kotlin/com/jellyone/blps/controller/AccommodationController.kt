package com.jellyone.blps.controller

import com.jellyone.blps.service.AccommodationService
import com.jellyone.blps.web.request.CreateAccommodationRequest
import com.jellyone.blps.web.response.toResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/accommodations")
@Tag(name = "Accommodations API")
@SecurityRequirement(name = "JWT")
class AccommodationController(
    private val accommodationService: AccommodationService
) {
    @PostMapping("")
    fun createAccommodation(
        @RequestBody response: CreateAccommodationRequest,
        principal: Principal
    ) = accommodationService.create(
        response.country,
        response.city,
        response.address,
        response.price,
        response.description,
        principal.name
    ).toResponse()

    @GetMapping("/{id}")
    fun getAccommodationById(@PathVariable id: Long) =
        accommodationService.getById(id).toResponse()

    @PutMapping("/{id}")
    fun updateAccommodationById(
        @PathVariable id: Long,
        @RequestBody response: CreateAccommodationRequest,
        principal: Principal
    ) = accommodationService.update(
        id,
        response.country,
        response.city,
        response.address,
        response.price,
        response.description,
        principal.name
    ).toResponse()

    @DeleteMapping("/{id}")
    fun deleteAccommodationById(
        @PathVariable id: Long,
        principal: Principal
    ) = accommodationService.delete(id, principal.name)
}