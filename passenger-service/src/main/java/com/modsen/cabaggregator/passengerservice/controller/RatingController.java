package com.modsen.cabaggregator.passengerservice.controller;

import com.modsen.cabaggregator.passengerservice.dto.AverageRatingDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingListDTO;
import com.modsen.cabaggregator.passengerservice.dto.RatingViewingDTO;
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
    public ResponseEntity<RatingViewingDTO> rate(@Valid @RequestBody RatingDTO ratingDTO,
                                                 @PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.rate(ratingDTO, id)
        );
    }

    @Operation(description = "Get all passengers ratings by ID")
    @GetMapping
    public ResponseEntity<RatingListDTO> getRatingsByPassengerId(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.getRatingsByPassengerId(id)
        );
    }

    @Operation(description = "Get passenger average rating by ID")
    @GetMapping("/average")
    public ResponseEntity<AverageRatingDTO> getAverageRating(@PathVariable UUID id) {
        return ResponseEntity.ok(
                ratingService.getAveragePassengerRating(id)
        );
    }

}
