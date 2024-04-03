package com.modsen.cabaggregator.rideservice.controller;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.rideservice.dto.AllRidesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreateRideRequest;
import com.modsen.cabaggregator.rideservice.dto.MessageResponse;
import com.modsen.cabaggregator.rideservice.dto.RideInfoResponse;
import com.modsen.cabaggregator.rideservice.dto.RideSortCriteria;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideSortField;
import com.modsen.cabaggregator.rideservice.service.RideService;
import com.modsen.cabaggregator.rideservice.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(Constants.RIDES_ENDPOINT)
@Tag(name = Constants.RIDES)
public class RideController {

    private final RideService rideService;

    @PreAuthorize("hasAnyRole('ROLE_PASSENGER')")
    @Operation(description = "Retrieving rides history for a passenger")
    @GetMapping(Constants.HISTORY_MAPPING)
    public AllRidesResponse findAllPassengerRides(@RequestParam(required = false, defaultValue = GlobalConstants.DEFAULT_PAGE) Integer page,
                                                  @RequestParam(required = false, defaultValue = GlobalConstants.DEFAULT_SIZE) Integer size,
                                                  @RequestParam(required = false) Sort.Direction sortOrder,
                                                  @RequestParam(required = false) RideSortField sortField,
                                                  @RequestParam UUID passengerId) {
        final RideSortCriteria sort = RideSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(RideSortField.DATE))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.DESC))
                .build();

        return rideService.findAllPassengerRides(passengerId, page, size, sort);
    }

    @Operation(description = "Getting a ride by ID")
    @GetMapping(Constants.ID_MAPPING)
    public RideInfoResponse getRideById(@PathVariable UUID id) {
        return rideService.getById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(description = "Removing a ride")
    @DeleteMapping(Constants.ID_MAPPING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRide(@PathVariable UUID id) {
        rideService.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_PASSENGER', 'ROLE_ADMIN')")
    @Operation(description = "Create a ride")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RideInfoResponse addRide(@RequestBody @Valid CreateRideRequest request) {
        return rideService.save(request);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    @Operation(description = "Change ride payment status", hidden = true)
    @PostMapping(Constants.ID_STATUS_MAPPING)
    public RideInfoResponse changeStatus(@PathVariable UUID id) {
        return rideService.changePaymentStatus(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    @Operation(description = "Ride rejection")
    @PutMapping(Constants.ID_REJECT_MAPPING)
    public MessageResponse rejectRide(@PathVariable UUID id) {
        return rideService.rejectRide(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")

    @Operation(description = "Start ride")
    @PutMapping(Constants.ID_START_MAPPING)
    public MessageResponse startRide(@PathVariable UUID id) {
        return rideService.startRide(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_ADMIN')")
    @Operation(description = "Finish ride")
    @PutMapping(Constants.ID_FINISH_MAPPING)
    public MessageResponse finishRide(@PathVariable UUID id) {
        return rideService.finishRide(id);
    }

}
