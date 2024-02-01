package com.modsen.cabaggregator.passengerservice.repository;

import com.modsen.cabaggregator.passengerservice.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, UUID> {
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
