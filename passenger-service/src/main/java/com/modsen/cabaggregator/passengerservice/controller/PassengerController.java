package com.modsen.cabaggregator.passengerservice.controller;

import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import com.modsen.cabaggregator.passengerservice.service.PassengerService;
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
@RequestMapping("/api/v1/passengers")
@Tag(name = "Passengers")
public class PassengerController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "10";

    private final PassengerService passengerService;

    @Operation(description = "Get all passengers")
    @GetMapping
    public ResponseEntity<AllPassengersResponse> findAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                         @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size,
                                                         @RequestParam(required = false) Sort.Direction sortOrder,
                                                         @RequestParam(required = false) PassengerSortField sortField) {
        final PassengerSortCriteria sort = PassengerSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(PassengerSortField.NAME))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.ASC))
                .build();

        return ResponseEntity.ok(
                passengerService.findAll(page, size, sort)
        );
    }

    @Operation(description = "Add new passenger")
    @PostMapping
    public ResponseEntity<PassengerResponse> save(@RequestBody @Valid CreatePassengerRequest request) {
        return new ResponseEntity<>(
                passengerService.save(request),
                HttpStatus.CREATED
        );
    }

    @Operation(description = "Delete passenger")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        passengerService.delete(id);
    }

    @Operation(description = "Get passenger by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PassengerResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                passengerService.findById(id)
        );
    }

    @Operation(description = "Update passenger by ID")
    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponse> updatePassenger(@PathVariable UUID id,
                                                             @RequestBody @Valid UpdatePassengerRequest request) {
        return ResponseEntity.ok(
                passengerService.update(id, request)
        );
    }

}
