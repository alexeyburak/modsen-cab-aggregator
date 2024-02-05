package com.modsen.cabaggregator.driverservice.mapper;

import com.modsen.cabaggregator.common.config.MapperConfig;
import com.modsen.cabaggregator.driverservice.dto.DriverResponse;
import com.modsen.cabaggregator.driverservice.model.Driver;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface DriverMapper {

    DriverResponse toDriverResponse(Driver driver);
}
