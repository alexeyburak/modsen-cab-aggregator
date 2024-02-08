package com.modsen.cabaggregator.rideservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllRidesResponse {
    private List<RideResponse> content;
    private int currentPageNumber;
    private int totalPages;
    private long totalElements;
}
