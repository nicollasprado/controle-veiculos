package com.nicollasprado.abstraction;

import com.nicollasprado.model.Driver;
import com.nicollasprado.model.Maintence;
import com.nicollasprado.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public abstract class userInterface implements userCommands {
    @Override
    public List<Maintence> getVehicleNextMaintenances(String vehicleName) {
        return List.of();
    }

    @Override
    public Driver getDriver(String cpf) {
        return null;
    }

    @Override
    public Vehicle getVehicle(String vehicleName) {
        return null;
    }

    @Override
    public void registerMaintenance(String vehicleName, LocalDateTime date, BigDecimal price) {

    }

    @Override
    public void registerDriver(String username, String cpf) {

    }

    @Override
    public void registerVehicle(String name, List<String> routesNames) {

    }

    @Override
    public void alterAdminLogin(String username, String password) {

    }

    @Override
    public boolean login(String username, String password) {
    }
}
