package com.modsen.cabaggregator.rideservice.controller;

import com.modsen.cabaggregator.rideservice.dto.AllRidesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreateRideRequest;
import com.modsen.cabaggregator.rideservice.dto.MessageResponse;
import com.modsen.cabaggregator.rideservice.dto.RideResponse;
import com.modsen.cabaggregator.rideservice.dto.RideSortCriteria;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideSortField;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideStatus;
import com.modsen.cabaggregator.rideservice.service.RideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/rides")
@Tag(name = "Rides")
public class RideController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "10";

    private final RideService rideService;

    @Operation(description = "Retrieving rides history for a passenger")
    @GetMapping("/history")
    public ResponseEntity<AllRidesResponse> findAllPassengerRides(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                                  @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size,
                                                                  @RequestParam(required = false) Sort.Direction sortOrder,
                                                                  @RequestParam(required = false) RideSortField sortField,
                                                                  @RequestParam UUID passengerId) {
        final RideSortCriteria sort = RideSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(RideSortField.DATE))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.DESC))
                .build();

        return ResponseEntity.ok(
                rideService.findAllPassengerRides(passengerId, page, size, sort)
        );
    }

    @Operation(description = "Getting a ride by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RideResponse> getRideById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                rideService.getById(id)
        );
    }

    @Operation(description = "Removing a ride")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable UUID id) {
        rideService.deleteById(id);
    }

    @Operation(description = "Create a ride")
    @PostMapping
    public ResponseEntity<RideResponse> addRide(@RequestBody @Valid CreateRideRequest request) {
        return new ResponseEntity<>(
                rideService.save(request),
                HttpStatus.CREATED
        );
    }

    @Operation(description = "Change ride status")
    @PostMapping("/{id}/status")
    public ResponseEntity<RideResponse> changeStatus(@PathVariable UUID id,
                                                     @RequestParam RideStatus status) {
        return ResponseEntity.ok(
                rideService.changeStatus(id, status)
        );
    }

    @Operation(description = "Ride rejection")
    @PutMapping("/{id}/reject")
    public ResponseEntity<MessageResponse> rejectRide(@PathVariable UUID id) {
        return ResponseEntity.ok(
                rideService.rejectRide(id)
        );
    }

    @Operation(description = "Start ride")
    @PutMapping("/{id}/start")
    public ResponseEntity<MessageResponse> startRide(@PathVariable UUID id) {
        return ResponseEntity.ok(
                rideService.startRide(id)
        );
    }

    @Operation(description = "Finish ride")
    @PutMapping("/{id}/finish")
    public ResponseEntity<MessageResponse> finishRide(@PathVariable UUID id) {
        return ResponseEntity.ok(
                rideService.finishRide(id)
        );
    }

}
