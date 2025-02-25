package com.nicollasprado.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Maintence {
    private Vehicle vehicle;
    private LocalDateTime dateTime;
    private BigDecimal price;

    public Maintence(Vehicle vehicle, LocalDateTime dateTime, BigDecimal price) {
        this.vehicle = vehicle;
        this.dateTime = dateTime;
        this.price = price;
    }
}
