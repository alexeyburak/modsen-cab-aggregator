package com.modsen.cabaggregator.driverservice.service;

import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverSortCriteria;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.exception.DriverNotFoundException;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;

import java.util.UUID;

public interface DriverService {
    AllDriversResponse findAll(Integer page, Integer size, DriverSortCriteria sort);

    DriverResponse save(CreateDriverRequest createDriverRequest);

    void delete(UUID id);

    DriverResponse findById(UUID id);

    DriverResponse update(UUID id, UpdateDriverRequest updateDriverRequest);

    Driver findEntityById(UUID id);

    void throwExceptionIfDriverDoesNotExist(UUID driverId) throws DriverNotFoundException;

    DriverResponse findAvailableById();

    DriverResponse updateStatus(UUID id, DriverStatus status);
}
