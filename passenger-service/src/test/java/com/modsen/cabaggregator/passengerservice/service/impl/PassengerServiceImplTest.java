package com.modsen.cabaggregator.passengerservice.service.impl;

import com.modsen.cabaggregator.passengerservice.client.PaymentServiceClient;
import com.modsen.cabaggregator.passengerservice.dto.AllPassengersResponse;
import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.CustomerRequest;
import com.modsen.cabaggregator.passengerservice.dto.PassengerResponse;
import com.modsen.cabaggregator.passengerservice.dto.PassengerSortCriteria;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.exception.EmailIsAlreadyExistsException;
import com.modsen.cabaggregator.passengerservice.exception.PassengerNotFoundException;
import com.modsen.cabaggregator.passengerservice.exception.PhoneIsAlreadyExistsException;
import com.modsen.cabaggregator.passengerservice.mapper.PassengerMapper;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import com.modsen.cabaggregator.passengerservice.repository.PassengerRepository;
import com.modsen.cabaggregator.passengerservice.service.PaymentService;
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

import static com.modsen.cabaggregator.passengerservice.util.TestUtils.EMAIL;
import static com.modsen.cabaggregator.passengerservice.util.TestUtils.PASSENGER_ID;
import static com.modsen.cabaggregator.passengerservice.util.TestUtils.PASSENGER_WAS_NOT_FOUND;
import static com.modsen.cabaggregator.passengerservice.util.TestUtils.PHONE;
import static com.modsen.cabaggregator.passengerservice.util.TestUtils.buildCreatePassengerRequest;
import static com.modsen.cabaggregator.passengerservice.util.TestUtils.buildPassenger;
import static com.modsen.cabaggregator.passengerservice.util.TestUtils.buildUpdatePassengerRequest;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImplTest {

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private PassengerMapper passengerMapper;

    @Mock
    private PaymentServiceClient paymentClient;

    @Mock
    private PaymentService paymentService;

    private int page;
    private int size;

    @BeforeEach
    void setUp() {
        page = 0;
        size = 10;
    }

    @Test
    void findAll_ShouldReturnAllPassengersResponse() {
        final List<Passenger> passengers = List.of(new Passenger(), new Passenger());
        final Page<Passenger> passengerPage = new PageImpl<>(passengers);
        final List<PassengerResponse> expectedResponses = List.of(new PassengerResponse(), new PassengerResponse());
        Mockito.when(passengerRepository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, PassengerSortField.EMAIL.getFiledName())))
        ).thenReturn(passengerPage);
        Mockito.when(passengerMapper.toPassengerResponse(passengers.get(0))).thenReturn(expectedResponses.get(0));
        Mockito.when(passengerMapper.toPassengerResponse(passengers.get(1))).thenReturn(expectedResponses.get(1));

        AllPassengersResponse response = passengerService.findAll(
                page, size, new PassengerSortCriteria(PassengerSortField.EMAIL, Sort.Direction.ASC)
        );

        Assertions.assertThat(response.getContent()).containsExactlyElementsOf(expectedResponses);
        Assertions.assertThat(response.getCurrentPageNumber()).isEqualTo(page);
        Assertions.assertThat(response.getTotalPages()).isEqualTo(passengerPage.getTotalPages());
        Assertions.assertThat(response.getTotalElements()).isEqualTo(passengerPage.getTotalElements());
        Mockito.verify(passengerMapper, Mockito.times(2)).toPassengerResponse(Mockito.any(Passenger.class));
    }

    @Test
    void save_ValidUser_ShouldSaveUserToRepository() {
        final CreatePassengerRequest request = buildCreatePassengerRequest();
        final Passenger passenger = buildPassenger();
        Mockito.when(passengerRepository.existsByEmail(EMAIL)).thenReturn(false);
        Mockito.when(passengerRepository.existsByPhone(PHONE)).thenReturn(false);
        Mockito.when(passengerRepository.save(Mockito.any(Passenger.class))).thenReturn(passenger);

        PassengerResponse actual = passengerService.save(request);

        Assertions.assertThat(actual).isEqualTo(passengerMapper.toPassengerResponse(passenger));
        Mockito.verify(paymentService).createCustomer(Mockito.any(CustomerRequest.class));
        Mockito.verify(passengerMapper, Mockito.times(2)).toPassengerResponse(passenger);
        Mockito.verify(passengerRepository).save(Mockito.any(Passenger.class));
        Mockito.verify(passengerRepository).existsByPhone(PHONE);
        Mockito.verify(passengerRepository).existsByEmail(EMAIL);
    }

    @Test
    void save_NotUniqueUserMail_ShouldThrowEmailIsAlreadyExistsException() {
        final CreatePassengerRequest request = buildCreatePassengerRequest();
        final Passenger passenger = buildPassenger();
        Mockito.when(passengerRepository.existsByEmail(EMAIL)).thenReturn(true);

        Assertions.assertThatThrownBy(() ->
                passengerService.save(request)
        ).isInstanceOf(EmailIsAlreadyExistsException.class).hasMessageContaining(EMAIL);

        Mockito.verify(paymentClient, Mockito.never()).createCustomer(Mockito.any(CustomerRequest.class));
        Mockito.verify(passengerMapper, Mockito.never()).toPassengerResponse(passenger);
        Mockito.verify(passengerRepository, Mockito.never()).save(Mockito.any(Passenger.class));
        Mockito.verify(passengerRepository, Mockito.never()).existsByPhone(PHONE);
        Mockito.verify(passengerRepository).existsByEmail(EMAIL);
    }

    @Test
    void save_NotUniqueUserPhone_ShouldThrowPhoneIsAlreadyExistsException() {
        final CreatePassengerRequest request = buildCreatePassengerRequest();
        final Passenger passenger = buildPassenger();
        Mockito.when(passengerRepository.existsByEmail(EMAIL)).thenReturn(false);
        Mockito.when(passengerRepository.existsByPhone(PHONE)).thenReturn(true);

        Assertions.assertThatThrownBy(() ->
                passengerService.save(request)
        ).isInstanceOf(PhoneIsAlreadyExistsException.class).hasMessageContaining(PHONE);

        Mockito.verify(paymentClient, Mockito.never()).createCustomer(Mockito.any(CustomerRequest.class));
        Mockito.verify(passengerMapper, Mockito.never()).toPassengerResponse(passenger);
        Mockito.verify(passengerRepository, Mockito.never()).save(Mockito.any(Passenger.class));
        Mockito.verify(passengerRepository).existsByPhone(PHONE);
        Mockito.verify(passengerRepository).existsByEmail(EMAIL);
    }

    @Test
    void delete_ShouldDeletePassengerFromRepository() {
        passengerService.delete(PASSENGER_ID);

        Mockito.verify(passengerRepository).deleteById(PASSENGER_ID);
    }

    @Test
    void findById_PassengerExists_ShouldReturnPassengerResponse() {
        final Passenger passenger = new Passenger();
        final PassengerResponse expected = new PassengerResponse();
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));
        Mockito.when(passengerMapper.toPassengerResponse(passenger)).thenReturn(expected);

        PassengerResponse actual = passengerService.findById(PASSENGER_ID);

        Assertions.assertThat(actual).isEqualTo(expected);
        Mockito.verify(passengerRepository).findById(PASSENGER_ID);
        Mockito.verify(passengerMapper).toPassengerResponse(passenger);
    }

    @Test
    void findById_PassengerNotExists_ShouldThrowPassengerNotFoundException() {
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        passengerService.findById(PASSENGER_ID)
                ).isInstanceOf(PassengerNotFoundException.class)
                .hasMessageContaining(String.format("Passenger with %s was not found", PASSENGER_ID));

        Mockito.verify(passengerRepository).findById(PASSENGER_ID);
    }

    @Test
    void update_ValidUser_ShouldUpdateUserAndSaveInRepository() {
        final UpdatePassengerRequest request = buildUpdatePassengerRequest();
        final Passenger passenger = new Passenger();
        Mockito.when(passengerRepository.existsByEmail(EMAIL)).thenReturn(false);
        Mockito.when(passengerRepository.existsByPhone(PHONE)).thenReturn(false);
        Mockito.when(passengerRepository.save(Mockito.any(Passenger.class))).thenReturn(passenger);
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));

        PassengerResponse actual = passengerService.update(PASSENGER_ID, request);

        Assertions.assertThat(actual).isEqualTo(passengerMapper.toPassengerResponse(passenger));
        Mockito.verify(passengerMapper, Mockito.times(2)).toPassengerResponse(passenger);
        Mockito.verify(passengerRepository).save(Mockito.any(Passenger.class));
        Mockito.verify(passengerRepository).existsByPhone(PHONE);
        Mockito.verify(passengerRepository).existsByEmail(EMAIL);
    }

    @Test
    void update_ExistingUserPhone_ShouldThrowPhoneIsAlreadyExistsException() {
        final UpdatePassengerRequest request = buildUpdatePassengerRequest();
        final Passenger passenger = new Passenger();
        Mockito.when(passengerRepository.existsByPhone(PHONE)).thenReturn(true);
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));

        Assertions.assertThatThrownBy(() ->
                passengerService.update(PASSENGER_ID, request)
        ).isInstanceOf(PhoneIsAlreadyExistsException.class);
        Mockito.verify(passengerRepository, Mockito.never()).save(Mockito.any(Passenger.class));
        Mockito.verify(passengerRepository).existsByPhone(PHONE);
        Mockito.verify(passengerRepository, Mockito.never()).existsByEmail(EMAIL);
    }

    @Test
    void update_ExistingUserEmail_ShouldThrowEmailIsAlreadyExistsException() {
        final UpdatePassengerRequest request = buildUpdatePassengerRequest();
        final Passenger passenger = new Passenger();
        Mockito.when(passengerRepository.existsByPhone(PHONE)).thenReturn(false);
        Mockito.when(passengerRepository.existsByEmail(EMAIL)).thenReturn(true);
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));

        Assertions.assertThatThrownBy(() ->
                passengerService.update(PASSENGER_ID, request)
        ).isInstanceOf(EmailIsAlreadyExistsException.class);
        Mockito.verify(passengerRepository, Mockito.never()).save(Mockito.any(Passenger.class));
        Mockito.verify(passengerRepository).existsByPhone(PHONE);
        Mockito.verify(passengerRepository).existsByEmail(EMAIL);
    }

    @Test
    void findEntityById_PassengerExists_ShouldReturnPassenger() {
        final Passenger passenger = new Passenger();
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.of(passenger));

        Passenger actual = passengerService.findEntityById(PASSENGER_ID);

        Assertions.assertThat(actual).isEqualTo(passenger);
        Mockito.verify(passengerRepository).findById(PASSENGER_ID);
    }

    @Test
    void findEntityById_PassengerNotExists_ShouldThrowPassengerNotFoundException() {
        Mockito.when(passengerRepository.findById(PASSENGER_ID)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                        passengerService.findEntityById(PASSENGER_ID)
                ).isInstanceOf(PassengerNotFoundException.class)
                .hasMessageContaining(String.format("Passenger with %s was not found", PASSENGER_ID));

        Mockito.verify(passengerRepository).findById(PASSENGER_ID);
    }

    @Test
    void throwExceptionIfPassengerDoesNotExist_PassengerNotExists_ShouldThrowPassengerNotFoundException() {
        Mockito.when(passengerRepository.existsById(PASSENGER_ID)).thenReturn(false);

        Assertions.assertThatThrownBy(() ->
                        passengerService.throwExceptionIfPassengerDoesNotExist(PASSENGER_ID)
                ).isInstanceOf(PassengerNotFoundException.class)
                .hasMessageContaining(String.format(PASSENGER_WAS_NOT_FOUND, PASSENGER_ID));

        Mockito.verify(passengerRepository).existsById(PASSENGER_ID);
    }

    @Test
    void throwExceptionIfPassengerDoesNotExist_PassengerExists_ShouldDoNothing() {
        Mockito.when(passengerRepository.existsById(PASSENGER_ID)).thenReturn(true);

        passengerService.throwExceptionIfPassengerDoesNotExist(PASSENGER_ID);

        Mockito.verify(passengerRepository).existsById(PASSENGER_ID);
    }

}
