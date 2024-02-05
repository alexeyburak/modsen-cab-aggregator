package com.modsen.cabaggregator.driverservice.dto;

import com.modsen.cabaggregator.driverservice.model.enumeration.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponse {
    private UUID id;
    private String name;
    private String surname;
    private String phone;
    private DriverStatus status;
    private Boolean active;
}
