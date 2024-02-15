package com.modsen.cabaggregator.passengerservice.controller;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
import com.modsen.cabaggregator.passengerservice.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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
@RequestMapping(Constants.PASSENGERS_ENDPOINT)
@Tag(name = Constants.PASSENGERS)
public class PassengerController {

    private final PassengerService passengerService;

    @Operation(description = "Get all passengers")
    @GetMapping
    public AllPassengersResponse findAll(@RequestParam(required = false, defaultValue = GlobalConstants.DEFAULT_PAGE) Integer page,
                                         @RequestParam(required = false, defaultValue = GlobalConstants.DEFAULT_SIZE) Integer size,
                                         @RequestParam(required = false) Sort.Direction sortOrder,
                                         @RequestParam(required = false) PassengerSortField sortField) {
        final PassengerSortCriteria sort = PassengerSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(PassengerSortField.NAME))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.ASC))
                .build();

        return passengerService.findAll(page, size, sort);
    }

    @Operation(description = "Add new passenger")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PassengerResponse save(@RequestBody @Valid CreatePassengerRequest request) {
        return passengerService.save(request);
    }

    @Operation(description = "Delete passenger")
    @DeleteMapping(Constants.ID_MAPPING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        passengerService.delete(id);
    }

    @Operation(description = "Get passenger by ID")
    @GetMapping(Constants.ID_MAPPING)
    public PassengerResponse findById(@PathVariable UUID id) {
        return passengerService.findById(id);
    }

    @Operation(description = "Update passenger by ID")
    @PutMapping(Constants.ID_MAPPING)
    public PassengerResponse updatePassenger(@PathVariable UUID id,
                                             @RequestBody @Valid UpdatePassengerRequest request) {
        return passengerService.update(id, request);
    }

}
