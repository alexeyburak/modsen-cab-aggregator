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
public class AllPromoCodesResponse {
    private List<PromoCodeResponse> content;
    private int currentPageNumber;
    private int totalPages;
    private long totalElements;
}
