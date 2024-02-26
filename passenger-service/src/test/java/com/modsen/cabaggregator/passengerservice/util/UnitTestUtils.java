package com.modsen.cabaggregator.passengerservice.util;

import com.modsen.cabaggregator.passengerservice.dto.CreatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.dto.UpdatePassengerRequest;
import com.modsen.cabaggregator.passengerservice.model.Passenger;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class UnitTestUtils {
    public static final String PASSENGER_WAS_NOT_FOUND = "Passenger with %s was not found";
    public static final UUID PASSENGER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String EMAIL = "email@gmail.com";
    public static final String PHONE = "80291112233";

    public static CreatePassengerRequest buildCreatePassengerRequest() {
        return CreatePassengerRequest.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .phone(PHONE)
                .build();
    }

    public static Passenger buildPassenger() {
        return Passenger.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .phone(PHONE)
                .active(true)
                .build();
    }

    public static UpdatePassengerRequest buildUpdatePassengerRequest() {
        return UpdatePassengerRequest.builder()
                .name(NAME)
                .surname(SURNAME)
                .email(EMAIL)
                .phone(PHONE)
                .build();
    }

}
