package com.modsen.cabaggregator.passengerservice.dto;

import com.modsen.cabaggregator.passengerservice.model.enumeration.PassengerSortField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PassengerSortCriteria {
    private PassengerSortField field;
    private Sort.Direction order;
}
