package com.modsen.cabaggregator.rideservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "promos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PromoCode {
    @Id
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "value", nullable = false)
    private BigDecimal value;
}
