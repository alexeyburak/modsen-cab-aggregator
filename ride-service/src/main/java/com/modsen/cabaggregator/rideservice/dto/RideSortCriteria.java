package com.modsen.cabaggregator.rideservice.dto;

import com.modsen.cabaggregator.rideservice.model.enumeration.RideSortField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideSortCriteria {
    private RideSortField field;
    private Sort.Direction order;
}
