package com.modsen.cabaggregator.driverservice.controller;

import com.modsen.cabaggregator.common.util.GlobalConstants;
import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.dto.DriverSortCriteria;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverSortField;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.driverservice.service.DriverService;
import com.modsen.cabaggregator.driverservice.util.Constants;
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
@RequestMapping(Constants.DRIVERS_ENDPOINT)
@Tag(name = Constants.DRIVERS)
public class DriverController {

    private final DriverService driverService;

    @Operation(description = "Get all drivers")
    @GetMapping
    public AllDriversResponse findAll(@RequestParam(required = false, defaultValue = GlobalConstants.DEFAULT_PAGE) Integer page,
                                      @RequestParam(required = false, defaultValue = GlobalConstants.DEFAULT_SIZE) Integer size,
                                      @RequestParam(required = false) Sort.Direction sortOrder,
                                      @RequestParam(required = false) DriverSortField sortField) {
        final DriverSortCriteria sort = DriverSortCriteria.builder()
                .field(Optional.ofNullable(sortField).orElse(DriverSortField.NAME))
                .order(Optional.ofNullable(sortOrder).orElse(Sort.Direction.ASC))
                .build();

        return driverService.findAll(page, size, sort);
    }

    @Operation(description = "Add new driver")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DriverResponse save(@RequestBody @Valid CreateDriverRequest request) {
        return driverService.save(request);
    }

    @Operation(description = "Delete driver")
    @DeleteMapping(Constants.ID_MAPPING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        driverService.delete(id);
    }

    @Operation(description = "Get driver by ID")
    @GetMapping(Constants.ID_MAPPING)
    public DriverResponse findById(@PathVariable UUID id) {
        return driverService.findById(id);
    }

    @Operation(description = "Get random available driver")
    @GetMapping(Constants.AVAILABLE_MAPPING)
    public DriverResponse findAvailableDriverById() {
        return driverService.findAvailableById();
    }

    @Operation(description = "Update driver status by ID")
    @PutMapping(Constants.ID_MAPPING + Constants.STATUS_MAPPING)
    public DriverResponse updateStatus(@PathVariable UUID id, @RequestParam DriverStatus status) {
        return driverService.updateStatus(id, status);
    }

    @Operation(description = "Update driver by ID")
    @PutMapping(Constants.ID_MAPPING)
    public DriverResponse update(@PathVariable UUID id,
                                 @RequestBody @Valid UpdateDriverRequest request) {
        return driverService.update(id, request);
    }

}
