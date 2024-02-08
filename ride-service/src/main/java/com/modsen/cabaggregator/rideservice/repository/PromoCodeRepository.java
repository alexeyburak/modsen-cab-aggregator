package com.modsen.cabaggregator.rideservice.repository;

import com.modsen.cabaggregator.rideservice.model.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, String> {
}
