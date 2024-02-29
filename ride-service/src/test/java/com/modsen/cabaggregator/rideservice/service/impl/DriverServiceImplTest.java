package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.client.DriverServiceClient;
import com.modsen.cabaggregator.rideservice.dto.DriverResponse;
import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @InjectMocks
    private DriverServiceImpl driverService;

    @Mock
    private DriverServiceClient driverServiceClient;

    @Test
    void getAvailableDriverId_ShouldCallDriverClientMethod() {
        final UUID expected = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Mockito.when(driverServiceClient.findAvailableDriverById())
                .thenReturn(
                        DriverResponse.builder()
                                .id(expected)
                                .build()
                );

        UUID actual = driverService.getAvailableDriverId();

        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(driverServiceClient).findAvailableDriverById();
    }

    @Test
    void changeDriverStatus_ShouldCallDriverClientMethod() {
        final UUID id = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Mockito.when(driverServiceClient.updateStatus(id, DriverStatus.AVAILABLE)).thenReturn(
                DriverResponse.builder()
                        .id(id)
                        .status(DriverStatus.AVAILABLE)
                        .build()
        );

        driverService.changeDriverStatus(id, DriverStatus.AVAILABLE);

        Mockito.verify(driverServiceClient).updateStatus(id, DriverStatus.AVAILABLE);
    }

}
