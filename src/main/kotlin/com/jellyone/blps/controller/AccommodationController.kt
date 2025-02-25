package com.jellyone.blps.controller

import com.jellyone.blps.service.AccommodationService
import com.jellyone.blps.web.request.CreateAccommodationRequest
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
    ) {
        accommodationService.create(
            response.country,
            response.city,
            response.address,
            response.price,
            response.description,
            principal.name
        )
    }

    @GetMapping("/{id}")
    fun getAccommodationById(@PathVariable id: Long) {
        accommodationService.getById(id)
    }
}