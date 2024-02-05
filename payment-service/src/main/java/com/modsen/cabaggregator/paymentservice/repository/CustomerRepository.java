package com.modsen.cabaggregator.paymentservice.repository;

import com.modsen.cabaggregator.paymentservice.model.PassengerCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<PassengerCustomer, UUID> {
}
