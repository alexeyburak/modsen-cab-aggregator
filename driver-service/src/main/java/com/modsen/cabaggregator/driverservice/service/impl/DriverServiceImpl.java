package com.modsen.cabaggregator.driverservice.service.impl;

import com.modsen.cabaggregator.common.util.PageRequestValidator;
import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverSortCriteria;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.exception.DriverNotFoundException;
import com.modsen.cabaggregator.driverservice.exception.NoAvailableDriversException;
import com.modsen.cabaggregator.driverservice.exception.PhoneIsAlreadyExistsException;
import com.modsen.cabaggregator.driverservice.mapper.DriverMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.driverservice.repository.DriverRepository;
import com.modsen.cabaggregator.driverservice.service.DriverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final DriverMapper driverMapper;

    @Override
    public AllDriversResponse findAll(Integer page, Integer size, DriverSortCriteria sort) {
        PageRequestValidator.validatePageRequestParameters(page, size);

        Sort sortBy = Sort.by(sort.getOrder(), sort.getField().getFieldName());
        Page<Driver> drivers = driverRepository.findAll(PageRequest.of(page, size, sortBy));
        return new AllDriversResponse(
                drivers.stream()
                        .map(driverMapper::toDriverResponse)
                        .toList(),
                page,
                drivers.getTotalPages(),
                drivers.getTotalElements()
        );
    }

    @Override
    @Transactional
    public DriverResponse save(CreateDriverRequest createDriverRequest) {
        final String phone = createDriverRequest.getPhone();
        throwExceptionIfPhoneExists(phone);

        log.info("Save driver. Phone: {}", phone);
        return driverMapper.toDriverResponse(
                driverRepository.save(
                        Driver.builder()
                                .name(createDriverRequest.getName())
                                .surname(createDriverRequest.getSurname())
                                .phone(phone)
                                .status(DriverStatus.AVAILABLE)
                                .active(true)
                                .build()
                )
        );
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        driverRepository.deleteById(id);
        log.info("Delete driver. ID: {}", id);
    }

    @Override
    public DriverResponse findById(UUID id) {
        log.debug("Get driver by ID: {}", id);
        return driverMapper.toDriverResponse(
                findEntityById(id)
        );
    }

    @Override
    @Transactional
    public DriverResponse update(UUID id, UpdateDriverRequest request) {
        Driver driver = findEntityById(id);

        updateDriverInfo(request, driver);
        updatePhone(request.getPhone(), driver);

        log.info("Update driver. ID {}", id);
        return driverMapper.toDriverResponse(
                driverRepository.save(driver)
        );
    }

    @Override
    public Driver findEntityById(UUID id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
    }

    @Override
    public void throwExceptionIfDriverDoesNotExist(UUID driverId) throws DriverNotFoundException {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException(driverId);
        }
    }

    @Override
    public DriverResponse findAvailableById() {
        return driverMapper.toDriverResponse(
                driverRepository.findAllByStatus(DriverStatus.AVAILABLE)
                        .stream()
                        .findAny()
                        .orElseThrow(NoAvailableDriversException::new)
        );
    }

    @Override
    public DriverResponse updateStatus(UUID id, DriverStatus status) {
        Driver driver = findEntityById(id);
        driver.setStatus(status);

        log.info("Update driver status. ID: {}, Status: {}", id, status);
        return driverMapper.toDriverResponse(
                driverRepository.save(
                        driverRepository.save(driver)
                )
        );
    }

    private void updateDriverInfo(UpdateDriverRequest request, Driver driver) {
        driver.setName(request.getName());
        driver.setSurname(request.getSurname());
        driver.setStatus(request.getStatus());
    }

    private void updatePhone(String updatedPhone, Driver driver) {
        if (!Objects.equals(driver.getPhone(), updatedPhone)) {
            throwExceptionIfPhoneExists(updatedPhone);
            driver.setPhone(updatedPhone);
        }
    }

    private void throwExceptionIfPhoneExists(String phone) throws PhoneIsAlreadyExistsException {
        if (driverRepository.existsByPhone(phone)) {
            throw new PhoneIsAlreadyExistsException(phone);
        }
    }

}
