package com.modsen.cabaggregator.driverservice.repository;

import com.modsen.cabaggregator.driverservice.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    boolean existsByPhone(String phone);
}
