package com.nicollasprado.model;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Entity;
import com.nicollasprado.annotations.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Route {
    @Id
    private int id;

    @Column(name = "start_location", nullable = false)
    private String startLocation;

    @Column(name = "final_location", nullable = false)
    private String finalLocation;

    @Column(name = "fuel_required_price", nullable = false)
    private BigDecimal fuelRequiredPrice;

    @Column(name = "other_costs", nullable = false)
    private BigDecimal otherCosts;


    public Route(String startLocation, String finalLocation, BigDecimal fuelRequiredPrice, BigDecimal otherCosts) {
        this.startLocation = startLocation;
        this.finalLocation = finalLocation;
        this.fuelRequiredPrice = fuelRequiredPrice;
        this.otherCosts = otherCosts;
    }
}
