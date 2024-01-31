package com.modsen.cabaggregator.passengerservice.controller;

import com.modsen.cabaggregator.passengerservice.dto.AllRatingsResponse;
import com.modsen.cabaggregator.passengerservice.dto.AverageRatingResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreateRatingRequest;
import com.modsen.cabaggregator.passengerservice.dto.RatingResponse;
import com.modsen.cabaggregator.passengerservice.service.RatingService;
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
@RequestMapping("/api/v1/passengers/{id}/rating")
@Tag(name = "Ratings")
public class RatingController {

    private final RatingService ratingService;

    @Operation(description = "Rate passenger by ID")
    @PostMapping
    public ResponseEntity<RatingResponse> rate(@Valid @RequestBody CreateRatingRequest request,
                                               @PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.rate(request, id)
        );
    }

    @Operation(description = "Get all passengers ratings by ID")
    @GetMapping
    public ResponseEntity<AllRatingsResponse> getRatingsByPassengerId(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.getRatingsByPassengerId(id)
        );
    }

    @Operation(description = "Get passenger average rating by ID")
    @GetMapping("/average")
    public ResponseEntity<AverageRatingResponse> getAverageRating(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.getAveragePassengerRating(id)
        );
    }

}
