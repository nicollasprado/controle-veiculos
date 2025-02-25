package com.nicollasprado.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Route {
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
