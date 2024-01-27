package com.modsen.cabaggregator.driverservice.dto;

import com.modsen.cabaggregator.driverservice.model.enumeration.DriverSortField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverSortCriteria {
    private DriverSortField field;
    private Sort.Direction order;
}
