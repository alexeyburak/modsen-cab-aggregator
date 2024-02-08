package com.modsen.cabaggregator.rideservice.repository;

import com.modsen.cabaggregator.rideservice.model.Ride;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RideRepository extends JpaRepository<Ride, UUID> {
    Page<Ride> findAllByPassengerId(UUID passengerId, Pageable pageable);
}
