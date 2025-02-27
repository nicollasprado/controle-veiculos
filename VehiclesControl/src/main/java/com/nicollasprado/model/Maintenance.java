package com.nicollasprado.model;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Entity;
import com.nicollasprado.annotations.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Maintenance {
    @Id
    private int id;

    @Column(name = "vehicle_name", nullable = false)
    private String vehicleName;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private BigDecimal price;

    public Maintenance(String vehicleName, LocalDateTime dateTime, BigDecimal price) {
        this.vehicleName = vehicleName;
        this.dateTime = dateTime;
        this.price = price;
    }
}
