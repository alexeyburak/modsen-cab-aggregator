package com.modsen.cabaggregator.rideservice.model;

import com.modsen.cabaggregator.rideservice.model.enumeration.RidePaymentMethod;
import com.modsen.cabaggregator.rideservice.model.enumeration.RideStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "rides")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ride {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pick_up", length = 100, nullable = false)
    private String pickUp;

    @Column(name = "date_at", nullable = false)
    private LocalDate dateAt;

    @Column(name = "time_at", nullable = false)
    private LocalTime timeAt;

    @Column(name = "destination", length = 100, nullable = false)
    private String destination;

    @Column(name = "passenger_id", nullable = false)
    private UUID passengerId;

    @Column(name = "driver_id", nullable = false)
    private UUID driverId;

    @Column(name = "initial_cost", nullable = false)
    private BigDecimal initialCost;

    @Column(name = "final_cost", nullable = false)
    private BigDecimal finalCost;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Column(name = "payment_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private RidePaymentMethod paymentMethod;

    @Column(name = "paid", nullable = false)
    private Boolean paid;

    public Boolean isPaid() {
        return paid;
    }

}
