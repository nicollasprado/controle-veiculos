package com.nicollasprado.model;

import com.nicollasprado.abstraction.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class Route extends Entity {
    private String startLocation;
    private String finalLocation;
    private BigDecimal fuelRequiredPrice;
    private BigDecimal otherCosts;

    public Route(String startLocation, String finalLocation, BigDecimal fuelRequiredPrice, BigDecimal otherCosts) {
        this.startLocation = startLocation;
        this.finalLocation = finalLocation;
        this.fuelRequiredPrice = fuelRequiredPrice;
        this.otherCosts = otherCosts;
    }
}
