package com.nicollasprado.model;

import com.nicollasprado.abstraction.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class Maintence extends Entity {
    private Vehicle vehicle;
    private LocalDateTime dateTime;
    private BigDecimal price;

    public Maintence(Vehicle vehicle, LocalDateTime dateTime, BigDecimal price) {
        this.vehicle = vehicle;
        this.dateTime = dateTime;
        this.price = price;
    }
}
