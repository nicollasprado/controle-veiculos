package com.nicollasprado.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MaintenceRegisterDTO(String vehicleName, LocalDateTime date, BigDecimal price) {
}
