package com.nicollasprado.dto;

import com.nicollasprado.model.Route;

import java.util.List;

public record VehicleRegisterDTO(String name, List<Route> routes) {
}
