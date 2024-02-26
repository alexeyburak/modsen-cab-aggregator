package com.modsen.cabaggregator.driverservice.util;

import com.modsen.cabaggregator.driverservice.dto.CreateDriverRequest;
import com.modsen.cabaggregator.driverservice.dto.UpdateDriverRequest;
import com.modsen.cabaggregator.driverservice.model.Driver;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class DriverServiceUtils {
    public static final String DRIVER_WAS_NOT_FOUND = "Driver with id %s was not found";
    public static final UUID DRIVER_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String PHONE = "80291112233";

    public static CreateDriverRequest buildCreateDriverRequest() {
        return CreateDriverRequest.builder()
                .name(NAME)
                .surname(SURNAME)
                .phone(PHONE)
                .build();
    }

    public static Driver buildDriver() {
        return Driver.builder()
                .name(NAME)
                .surname(SURNAME)
                .phone(PHONE)
                .active(true)
                .build();
    }

    public static UpdateDriverRequest buildUpdateDriverRequest() {
        return UpdateDriverRequest.builder()
                .name(NAME)
                .surname(SURNAME)
                .phone(PHONE)
                .build();
    }

}
