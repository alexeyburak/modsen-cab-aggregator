package com.modsen.cabaggregator.driverservice.service.impl;

import com.modsen.cabaggregator.driverservice.dto.AllDriversResponse;
import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.dto.DriverSortCriteria;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.exception.DriverNotFoundException;
import com.modsen.cabaggregator.driverservice.exception.PhoneIsAlreadyExistsException;
import com.modsen.cabaggregator.driverservice.mapper.DriverMapper;
import com.modsen.cabaggregator.driverservice.model.Driver;
import com.modsen.cabaggregator.driverservice.model.enumeration.DriverSortField;
import com.modsen.cabaggregator.driverservice.repository.DriverRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.modsen.cabaggregator.driverservice.util.DriverServiceUtils.DRIVER_ID;
import static com.modsen.cabaggregator.driverservice.util.DriverServiceUtils.DRIVER_WAS_NOT_FOUND;
import static com.modsen.cabaggregator.driverservice.util.DriverServiceUtils.PHONE;
import static com.modsen.cabaggregator.driverservice.util.DriverServiceUtils.buildCreateDriverRequest;
import static com.modsen.cabaggregator.driverservice.util.DriverServiceUtils.buildDriver;
import static com.modsen.cabaggregator.driverservice.util.DriverServiceUtils.buildUpdateDriverRequest;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private DriverMapper driverMapper;

    private int page;
    private int size;

    @BeforeEach
    void setUp() {
        page = 0;
        size = 10;
    }

    @Test
    void findAll_ShouldReturnAllDriversResponse() {
        final List<Driver> drivers = List.of(new Driver(), new Driver());
        final Page<Driver> driverPage = new PageImpl<>(drivers);
        final List<DriverResponse> expectedResponses = List.of(new DriverResponse(), new DriverResponse());
        Mockito.when(driverRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, DriverSortField.NAME.getFieldName())))
        ).thenReturn(driverPage);
        Mockito.when(driverMapper.toDriverResponse(drivers.get(0))).thenReturn(expectedResponses.get(0));
        Mockito.when(driverMapper.toDriverResponse(drivers.get(1))).thenReturn(expectedResponses.get(1));

        AllDriversResponse response = driverService.findAll(
                page, size, new DriverSortCriteria(DriverSortField.NAME, Sort.Direction.ASC)
        );

        Assertions.assertThat(response.getContent()).containsExactlyElementsOf(expectedResponses);
        Assertions.assertThat(response.getCurrentPageNumber()).isEqualTo(page);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(driverPage.getTotalPages());
        Assertions.assertThat(response.getTotalElements()).isEqualTo(driverPage.getTotalElements());
        Mockito.verify(driverMapper, Mockito.times(2)).toDriverResponse(Mockito.any(Driver.class));
    }

    @Test
    void save_ValidUser_ShouldSaveDriverToRepository() {
        final CreateDriverRequest request = buildCreateDriverRequest();
        final Driver driver = buildDriver();
        Mockito.when(driverRepository.existsByPhone(PHONE)).thenReturn(false);
        Mockito.when(driverRepository.save(Mockito.any(Driver.class))).thenReturn(driver);

        DriverResponse actual = driverService.save(request);

        Assertions.assertThat(actual).isEqualTo(driverMapper.toDriverResponse(driver));
        Mockito.verify(driverMapper, Mockito.times(2)).toDriverResponse(driver);
        Mockito.verify(driverRepository).save(Mockito.any(Driver.class));
        Mockito.verify(driverRepository).existsByPhone(PHONE);
    }

    @Test
    void save_NotUniqueDriverPhone_ShouldThrowPhoneIsAlreadyExistsException() {
        final CreateDriverRequest request = buildCreateDriverRequest();
        final Driver driver = buildDriver();
        Mockito.when(driverRepository.existsByPhone(PHONE)).thenReturn(true);

        Assertions.assertThatThrownBy(() ->
                driverService.save(request)
        ).isInstanceOf(PhoneIsAlreadyExistsException.class).hasMessageContaining(PHONE);

        Mockito.verify(driverMapper, Mockito.never()).toDriverResponse(driver);
        Mockito.verify(driverRepository, Mockito.never()).save(Mockito.any(Driver.class));
        Mockito.verify(driverRepository).existsByPhone(PHONE);
    }

    @Test
    void delete_ShouldDeleteDriverFromRepository() {
        driverService.delete(DRIVER_ID);

        Mockito.verify(driverRepository).deleteById(DRIVER_ID);
    }

    @Test
    void findById_DriverExists_ShouldReturnDriverResponse() {
        final Driver driver = new Driver();
        final DriverResponse expected = new DriverResponse();
        Mockito.when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));
        Mockito.when(driverMapper.toDriverResponse(driver)).thenReturn(expected);

        DriverResponse actual = driverService.findById(DRIVER_ID);

        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(driverRepository).findById(DRIVER_ID);
        Mockito.verify(driverMapper).toDriverResponse(driver);
    }

    @Test
    void findById_DriverNotExists_ShouldThrowDriverNotFoundException() {
        Mockito.when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        driverService.findById(DRIVER_ID)
                ).isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining(String.format("Driver with id %s was not found", DRIVER_ID));

        Mockito.verify(driverRepository).findById(DRIVER_ID);
    }

    @Test
    void update_ValidDriver_ShouldUpdateDriverAndSaveInRepository() {
        final UpdateDriverRequest request = buildUpdateDriverRequest();
        final Driver driver = new Driver();
        Mockito.when(driverRepository.existsByPhone(PHONE)).thenReturn(false);
        Mockito.when(driverRepository.save(Mockito.any(Driver.class))).thenReturn(driver);
        Mockito.when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));

        DriverResponse actual = driverService.update(DRIVER_ID, request);

        Assertions.assertThat(actual).isEqualTo(driverMapper.toDriverResponse(driver));
        Mockito.verify(driverMapper, Mockito.times(2)).toDriverResponse(driver);
        Mockito.verify(driverRepository).save(Mockito.any(Driver.class));
        Mockito.verify(driverRepository).existsByPhone(PHONE);
    }

    @Test
    void update_ExistingDriverPhone_ShouldThrowPhoneIsAlreadyExistsException() {
        final UpdateDriverRequest request = buildUpdateDriverRequest();
        final Driver driver = new Driver();
        Mockito.when(driverRepository.existsByPhone(PHONE)).thenReturn(true);
        Mockito.when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));

        Assertions.assertThatThrownBy(() ->
                driverService.update(DRIVER_ID, request)
        ).isInstanceOf(PhoneIsAlreadyExistsException.class);
        Mockito.verify(driverRepository, Mockito.never()).save(Mockito.any(Driver.class));
        Mockito.verify(driverRepository).existsByPhone(PHONE);
    }

    @Test
    void findEntityById_DriverExists_ShouldReturnDriver() {
        final Driver driver = buildDriver();
        Mockito.when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.of(driver));

        Driver actual = driverService.findEntityById(DRIVER_ID);

        Assertions.assertThat(actual).isEqualTo(driver);
        Mockito.verify(driverRepository).findById(DRIVER_ID);
    }

    @Test
    void findEntityById_DriverNotExists_ShouldThrowDriverNotFoundException() {
        Mockito.when(driverRepository.findById(DRIVER_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        driverService.findEntityById(DRIVER_ID)
                ).isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining(String.format("Driver with id %s was not found", DRIVER_ID));

        Mockito.verify(driverRepository).findById(DRIVER_ID);
    }

    @Test
    void throwExceptionIfDriverDoesNotExist_DriverNotExists_ShouldThrowDriverNotFoundException() {
        Mockito.when(driverRepository.existsById(DRIVER_ID)).thenReturn(false);

        Assertions.assertThatThrownBy(() ->
                        driverService.throwExceptionIfDriverDoesNotExist(DRIVER_ID)
                ).isInstanceOf(DriverNotFoundException.class)
                .hasMessageContaining(String.format(DRIVER_WAS_NOT_FOUND, DRIVER_ID));

        Mockito.verify(driverRepository).existsById(DRIVER_ID);
    }

    @Test
    void throwExceptionIfDriverDoesNotExist_DriverExists_ShouldDoNothing() {
        Mockito.when(driverRepository.existsById(DRIVER_ID)).thenReturn(true);

        driverService.throwExceptionIfDriverDoesNotExist(DRIVER_ID);

        Mockito.verify(driverRepository).existsById(DRIVER_ID);
    }

}
