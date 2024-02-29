package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.rideservice.client.DriverServiceClient;
import com.modsen.cabaggregator.rideservice.client.PassengerServiceClient;
import com.modsen.cabaggregator.rideservice.dto.AllRidesResponse;
import com.modsen.cabaggregator.rideservice.dto.DriverResponse;
import com.modsen.cabaggregator.rideservice.dto.MessageResponse;
import com.modsen.cabaggregator.rideservice.dto.PassengerResponse;
import com.modsen.cabaggregator.rideservice.dto.RideInfoResponse;
import com.modsen.cabaggregator.rideservice.dto.RideResponse;
import com.modsen.cabaggregator.rideservice.dto.RideSortCriteria;
import com.modsen.cabaggregator.rideservice.exception.ImpossibleRideRejectionException;
import com.modsen.cabaggregator.rideservice.exception.RideWasNotPaidException;
import com.modsen.cabaggregator.rideservice.exception.RideWasNotStartedException;
import com.modsen.cabaggregator.rideservice.mapper.RideMapper;
import com.modsen.cabaggregator.rideservice.model.Ride;
import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.rideservice.model.enumeration.MessageResponseCode;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideSortField;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideStatus;
import com.modsen.cabaggregator.rideservice.repository.RideRepository;
import com.modsen.cabaggregator.rideservice.service.DriverService;
import com.modsen.cabaggregator.rideservice.service.NotificationBrokerService;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RideServiceImplTest {

    private static final UUID RIDE_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverService driverService;

    @Mock
    private RideMapper rideMapper;

    @Mock
    private DriverServiceClient driverClient;

    @Mock
    private PassengerServiceClient passengerClient;

    @Mock
    private NotificationBrokerService notificationService;

    @InjectMocks
    private RideServiceImpl rideService;

    private PassengerResponse passengerResponse;

    @BeforeEach
    void setUp() {
        passengerResponse = PassengerResponse.builder()
                .email("email")
                .build();
    }

    @Test
    void findAllPassengerRides_ShouldReturnPassengerRides() {
        UUID passengerId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        RideSortCriteria sort = new RideSortCriteria(RideSortField.DATE, Sort.Direction.DESC);
        Ride ride1 = new Ride();
        Ride ride2 = new Ride();
        List<Ride> rides = Arrays.asList(ride1, ride2);
        Page<Ride> ridePage = new PageImpl<>(rides);
        Mockito.when(rideRepository.findAllByPassengerId(Mockito.eq(passengerId), Mockito.any(PageRequest.class))).thenReturn(ridePage);
        Mockito.when(rideMapper.toRideResponse(ride1)).thenReturn(new RideResponse());
        Mockito.when(rideMapper.toRideResponse(ride2)).thenReturn(new RideResponse());

        AllRidesResponse response = rideService.findAllPassengerRides(passengerId, 0, 10, sort);

        Mockito.verify(rideRepository).findAllByPassengerId(Mockito.eq(passengerId), Mockito.any(PageRequest.class));
        Mockito.verify(rideMapper, Mockito.times(2)).toRideResponse(Mockito.any(Ride.class));
        assertThat(response.getContent()).hasSize(rides.size());
        assertThat(response.getCurrentPageNumber()).isZero();
        assertThat(response.getTotalPages()).isEqualTo(ridePage.getTotalPages());
        assertThat(response.getTotalElements()).isEqualTo(ridePage.getTotalElements());
    }

    @Test
    void getById_ShouldReturnRideInfoResponse() {
        Ride ride = Ride.builder()
                .id(RIDE_ID)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));
        Mockito.when(driverClient.findById(ride.getDriverId())).thenReturn(new DriverResponse());
        Mockito.when(passengerClient.findById(ride.getPassengerId())).thenReturn(new PassengerResponse());
        Mockito.when(rideMapper.toRideInfoResponse(ride, new DriverResponse(), new PassengerResponse())).thenReturn(new RideInfoResponse());

        RideInfoResponse response = rideService.getById(RIDE_ID);

        Mockito.verify(rideRepository).findById(RIDE_ID);
        Mockito.verify(driverClient).findById(ride.getDriverId());
        Mockito.verify(passengerClient).findById(ride.getPassengerId());
        Mockito.verify(rideMapper).toRideInfoResponse(ride, new DriverResponse(), new PassengerResponse());
        assertThat(response).isNotNull();
    }

    @Test
    void deleteById_ShouldDeleteFromRepository() {
        rideService.deleteById(RIDE_ID);

        Mockito.verify(rideRepository).deleteById(RIDE_ID);
    }

    @Test
    void rejectRide_ShouldRejectRideAndUpdateStatus() {
        Ride ride = Ride.builder()
                .status(RideStatus.NOT_PAID)
                .paid(false)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));
        Mockito.doNothing().when(driverService).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(notificationService).send(Mockito.any(), Mockito.any());
        Mockito.when(passengerClient.findById(Mockito.any())).thenReturn(passengerResponse);

        MessageResponse actual = rideService.rejectRide(RIDE_ID);

        Assertions.assertThat(actual.getMessage()).isEqualTo(MessageResponseCode.REJECT_RIDE.getGlobalCode());
        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.REJECTED);
        Mockito.verify(rideRepository).save(ride);
        Mockito.verify(driverService).changeDriverStatus(ride.getId(), DriverStatus.AVAILABLE);
        Mockito.verify(notificationService).send(Mockito.any(), Mockito.any());
    }

    @Test
    void rejectRide_ShouldThrowExceptionWhenRideNotEligibleForRejection() {
        Ride ride = Ride.builder()
                .status(RideStatus.PAID)
                .paid(true)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(ImpossibleRideRejectionException.class, () ->
                rideService.rejectRide(RIDE_ID)
        );
        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.PAID);
        Mockito.verify(rideRepository, Mockito.never()).save(ride);
        Mockito.verify(driverService, Mockito.never()).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.verify(notificationService, Mockito.never()).send(Mockito.any(), Mockito.any());
    }

    @Test
    void startRide_ShouldStartRideAndUpdateStatus() {
        Ride ride = Ride.builder()
                .status(RideStatus.PAID)
                .paid(true)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));
        Mockito.doNothing().when(driverService).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(notificationService).send(Mockito.any(), Mockito.any());
        Mockito.when(passengerClient.findById(Mockito.any())).thenReturn(passengerResponse);

        MessageResponse actual = rideService.startRide(RIDE_ID);

        Assertions.assertThat(actual.getMessage()).isEqualTo(MessageResponseCode.START_RIDE.getGlobalCode());
        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.STARTED);
        Mockito.verify(rideRepository).save(ride);
        Mockito.verify(driverService).changeDriverStatus(ride.getId(), DriverStatus.UNAVAILABLE);
        Mockito.verify(notificationService).send(Mockito.any(), Mockito.any());
    }

    @Test
    void startRide_ShouldThrowRideWasNotPaidException() {
        Ride ride = Ride.builder()
                .status(RideStatus.NOT_PAID)
                .paid(false)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(RideWasNotPaidException.class, () ->
                rideService.startRide(RIDE_ID)
        );
        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.NOT_PAID);
        Mockito.verify(rideRepository, Mockito.never()).save(ride);
        Mockito.verify(driverService, Mockito.never()).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.verify(notificationService, Mockito.never()).send(Mockito.any(), Mockito.any());
    }

    @Test
    void finishRide_ShouldFinishRideAndUpdateStatus() {
        Ride ride = Ride.builder()
                .status(RideStatus.STARTED)
                .paid(true)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));
        Mockito.doNothing().when(driverService).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.doNothing().when(notificationService).send(Mockito.any(), Mockito.any());
        Mockito.when(passengerClient.findById(Mockito.any())).thenReturn(passengerResponse);

        MessageResponse actual = rideService.finishRide(RIDE_ID);

        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.FINISHED);
        Assertions.assertThat(actual.getMessage()).isEqualTo(MessageResponseCode.FINISH_RIDE.getGlobalCode());
        Mockito.verify(rideRepository).save(ride);
        Mockito.verify(driverService).changeDriverStatus(ride.getId(), DriverStatus.AVAILABLE);
        Mockito.verify(notificationService).send(Mockito.any(), Mockito.any());
    }

    @Test
    void finishRide_ShouldThrowRideWasNotPaidException() {
        Ride ride = Ride.builder()
                .status(RideStatus.STARTED)
                .paid(false)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(RideWasNotPaidException.class, () ->
                rideService.finishRide(RIDE_ID)
        );
        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.STARTED);
        Mockito.verify(rideRepository, Mockito.never()).save(ride);
        Mockito.verify(driverService, Mockito.never()).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.verify(notificationService, Mockito.never()).send(Mockito.any(), Mockito.any());
    }

    @Test
    void finishRide_ShouldThrowRideWasNotStartedException() {
        Ride ride = Ride.builder()
                .status(RideStatus.PAID)
                .paid(true)
                .build();
        Mockito.when(rideRepository.findById(RIDE_ID)).thenReturn(Optional.of(ride));

        assertThrows(RideWasNotStartedException.class, () ->
                rideService.finishRide(RIDE_ID)
        );
        Assertions.assertThat(ride.getStatus()).isEqualTo(RideStatus.PAID);
        Mockito.verify(rideRepository, Mockito.never()).save(ride);
        Mockito.verify(driverService, Mockito.never()).changeDriverStatus(Mockito.any(), Mockito.any());
        Mockito.verify(notificationService, Mockito.never()).send(Mockito.any(), Mockito.any());
    }

}
