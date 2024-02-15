package com.modsen.cabaggregator.rideservice.service.impl;

import com.modsen.cabaggregator.common.util.PageRequestValidator;
import com.modsen.cabaggregator.rideservice.client.DriverServiceClient;
import com.modsen.cabaggregator.rideservice.client.PassengerServiceClient;
import com.modsen.cabaggregator.rideservice.dto.AllRidesResponse;
import com.modsen.cabaggregator.rideservice.dto.CreateRideRequest;
import com.modsen.cabaggregator.rideservice.dto.MessageResponse;
import com.modsen.cabaggregator.rideservice.dto.RideInfoResponse;
import com.modsen.cabaggregator.rideservice.dto.RideSortCriteria;
import com.modsen.cabaggregator.rideservice.exception.ImpossibleRideRejectionException;
import com.modsen.cabaggregator.rideservice.exception.RideNotFoundException;
import com.modsen.cabaggregator.rideservice.exception.RideWasNotPaidException;
import com.modsen.cabaggregator.rideservice.exception.RideWasNotStartedException;
import com.modsen.cabaggregator.rideservice.mapper.RideMapper;
import com.modsen.cabaggregator.rideservice.model.Ride;
import com.modsen.cabaggregator.rideservice.model.enumeration.DriverStatus;
import com.modsen.cabaggregator.rideservice.model.enumeration.MessageResponseCode;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideStatus;
import com.modsen.cabaggregator.rideservice.repository.RideRepository;
import com.modsen.cabaggregator.rideservice.service.DriverService;
import com.modsen.cabaggregator.rideservice.service.RideCostService;
import com.modsen.cabaggregator.rideservice.service.RideService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverService driverService;
    private final RideCostService rideCostService;
    private final RideMapper rideMapper;
    private final DriverServiceClient driverClient;
    private final PassengerServiceClient passengerClient;

    @Override
    public AllRidesResponse findAllPassengerRides(UUID passengerId, Integer page, Integer size, RideSortCriteria sort) {
        PageRequestValidator.validatePageRequestParameters(page, size);
        Sort sortBy = Sort.by(sort.getOrder(), sort.getField().getFieldName());
        Page<Ride> rides = rideRepository.findAllByPassengerId(
                passengerId,
                PageRequest.of(page, size, sortBy)
        );
        return new AllRidesResponse(
                rides.getContent()
                        .stream()
                        .map(rideMapper::toRideResponse)
                        .toList(),
                page,
                rides.getTotalPages(),
                rides.getTotalElements()
        );
    }

    @Override
    public RideInfoResponse getById(UUID id) {
        Ride ride = findEntityById(id);
        return rideMapper.toRideInfoResponse(
                ride,
                driverClient.findById(ride.getDriverId()),
                passengerClient.findById(ride.getPassengerId())
        );
    }

    @Override
    public void deleteById(UUID id) {
        rideRepository.deleteById(id);
        log.info("Delete ride by ID: {}", id);
    }

    @Override
    public RideInfoResponse save(CreateRideRequest request) {
        final BigDecimal initialCost = rideCostService.getInitialRideCost();
        final UUID availableDriverId = driverService.getAvailableDriverId();
        final UUID passengerId = request.getPassengerId();

        driverService.changeDriverStatus(availableDriverId, DriverStatus.UNAVAILABLE);
        log.info("Save new ride. Driver ID: {}, Passenger ID: {}", availableDriverId, passengerId);
        return rideMapper.toRideInfoResponse(
                rideRepository.save(
                        Ride.builder()
                                .pickUp(request.getPickUp())
                                .dateAt(LocalDate.now())
                                .timeAt(LocalTime.now())
                                .destination(request.getDestination())
                                .passengerId(passengerId)
                                .driverId(availableDriverId)
                                .initialCost(initialCost)
                                .finalCost(rideCostService.getFinalRideCostWithPromo(initialCost, request.getPromoCode()))
                                .status(RideStatus.NOT_PAID)
                                .paymentMethod(request.getPaymentMethod())
                                .paid(false)
                                .build()),
                driverClient.findById(availableDriverId),
                passengerClient.findById(passengerId)
        );
    }

    @Override
    public MessageResponse rejectRide(UUID id) {
        Ride ride = findEntityById(id);

        if (!ride.getStatus().equals(RideStatus.NOT_PAID) && ride.isPaid()) {
            throw new ImpossibleRideRejectionException(id);
        }
        ride.setStatus(RideStatus.REJECTED);

        saveRideAndUpdateDriverStatus(ride, DriverStatus.AVAILABLE);
        log.info("Reject ride. ID: {}", id);
        return new MessageResponse(
                MessageResponseCode.REJECT_RIDE.getGlobalCode()
        );
    }

    @Override
    public MessageResponse startRide(UUID id) {
        Ride ride = findEntityById(id);

        validateIfRideWasPaid(ride);
        ride.setStatus(RideStatus.STARTED);

        saveRideAndUpdateDriverStatus(ride, DriverStatus.UNAVAILABLE);
        log.info("Start ride. ID: {}", id);
        return new MessageResponse(
                MessageResponseCode.START_RIDE.getGlobalCode()
        );
    }

    @Override
    public MessageResponse finishRide(UUID id) {
        Ride ride = findEntityById(id);

        if (!ride.getStatus().equals(RideStatus.STARTED)) {
            throw new RideWasNotStartedException(id);
        }
        validateIfRideWasPaid(ride);
        ride.setStatus(RideStatus.FINISHED);

        saveRideAndUpdateDriverStatus(ride, DriverStatus.AVAILABLE);
        log.info("Start ride. ID: {}", id);
        return new MessageResponse(
                MessageResponseCode.FINISH_RIDE.getGlobalCode()
        );
    }

    @Override
    public RideInfoResponse changePaymentStatus(UUID id) {
        Ride ride = findEntityById(id);
        ride.setStatus(RideStatus.PAID);
        ride.setPaid(true);

        log.info("Change ride status. ID: {}, Status: {}", id, RideStatus.PAID);
        return rideMapper.toRideInfoResponse(
                rideRepository.save(ride),
                driverClient.findById(ride.getDriverId()),
                passengerClient.findById(ride.getPassengerId())
        );
    }

    private Ride findEntityById(UUID id) {
        return rideRepository.findById(id)
                .orElseThrow(() -> new RideNotFoundException(id));
    }

    private void saveRideAndUpdateDriverStatus(Ride ride, DriverStatus status) {
        driverService.changeDriverStatus(ride.getDriverId(), status);
        rideRepository.save(ride);
    }

    private void validateIfRideWasPaid(Ride ride) throws RideWasNotPaidException {
        if (!ride.getStatus().equals(RideStatus.PAID) && !ride.isPaid()) {
            throw new RideWasNotPaidException(ride.getId());
        }
    }

}
