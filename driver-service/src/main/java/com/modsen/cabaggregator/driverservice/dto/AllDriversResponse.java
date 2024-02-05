package com.modsen.cabaggregator.driverservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllDriversResponse {
    private List<DriverResponse> content;
    private int currentPageNumber;
    private int totalPages;
    private long totalElements;
}
