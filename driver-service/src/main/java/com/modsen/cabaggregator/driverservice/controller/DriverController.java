package com.modsen.cabaggregator.driverservice.controller;

import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.dto.DriverSortCriteria;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverSortField;
import com.modsen.cabaggregator.driverservice.service.DriverService;
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
@RequestMapping("/api/v1/drivers")
@Tag(name = "Drivers")
public class DriverController {

    public static final String DEFAULT_PAGE = "0";
    public static final String DEFAULT_SIZE = "10";

    private final DriverService driverService;

    @Operation(description = "Get all drivers")
    @GetMapping
    public ResponseEntity<AllDriversResponse> findAll(@RequestParam(required = false, defaultValue = DEFAULT_PAGE) Integer page,
                                                      @RequestParam(required = false, defaultValue = DEFAULT_SIZE) Integer size,
                                                      @RequestParam(required = false) Sort.Direction sortOrder,
                                                      @RequestParam(required = false) DriverSortField sortField) {
        final DriverSortCriteria sort = DriverSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(DriverSortField.NAME))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.ASC))
                .build();

        return ResponseEntity.ok(
                driverService.findAll(page, size, sort)
        );
    }

    @Operation(description = "Add new driver")
    @PostMapping
    public ResponseEntity<DriverResponse> save(@RequestBody @Valid CreateDriverRequest request) {
        return new ResponseEntity<>(
                driverService.save(request),
                HttpStatus.CREATED
        );
    }

    @Operation(description = "Delete driver")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        driverService.delete(id);
    }

    @Operation(description = "Get driver by ID")
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
                driverService.findById(id)
        );
    }

    @Operation(description = "Update driver by ID")
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> update(@PathVariable UUID id,
                                                 @RequestBody @Valid UpdateDriverRequest request) {
        return ResponseEntity.ok(
                driverService.update(id, request)
        );
    }

}