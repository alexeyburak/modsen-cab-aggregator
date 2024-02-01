package com.modsen.cabaggregator.passengerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllPassengersResponse {
    private List<PassengerResponse> content;
    private int currentPageNumber;
    private int totalPages;
    private long totalElements;
}
