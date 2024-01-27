package com.modsen.cabaggregator.driverservice.repository;

import com.modsen.cabaggregator.driverservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findRatingsByDriverId(UUID driverId);
}
