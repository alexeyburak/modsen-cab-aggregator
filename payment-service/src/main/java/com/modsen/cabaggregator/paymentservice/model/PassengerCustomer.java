package com.modsen.cabaggregator.paymentservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerCustomer {
    @Id
    @Column(name = "passenger_id")
    private UUID passengerId;

    @Column(name = "customer_id", nullable = false)
    private String customerId;
}
