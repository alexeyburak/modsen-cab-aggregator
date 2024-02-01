package com.modsen.cabaggregator.driverservice.controller;

import com.modsen.cabaggregator.driverservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.driverservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.driverservice.dto.RatingResponse;
import com.modsen.cabaggregator.driverservice.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/drivers/{id}/rating")
@Tag(name = "Ratings")
public class RatingController {

    private final RatingService ratingService;

    @Operation(description = "Rate driver by ID")
    @PostMapping
    public ResponseEntity<RatingResponse> rate(@Valid @RequestBody CreateRatingRequest request,
                                               @PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.rate(request, id)
        );
    }

    @Operation(description = "Get all drivers ratings by ID")
    @GetMapping
    public ResponseEntity<AllRatingsResponse> getRatingsByDriverId(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.getRatingsByDriverId(id)
        );
    }

    @Operation(description = "Get driver average rating by ID")
    @GetMapping("/average")
    public ResponseEntity<AverageRatingResponse> getAverageDriverRating(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.getAverageDriverRating(id)
        );
    }

}