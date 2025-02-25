package com.nicollasprado.abstraction;

import com.nicollasprado.model.Driver;
import com.nicollasprado.model.Maintence;
import com.nicollasprado.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface userCommands {
    boolean login(String username, String password);
    void alterAdminLogin(String username, String password);
    void registerVehicle(String name, List<String> routesNames);
    void registerDriver(String username, String cpf);
    void registerMaintenance(String vehicleName, LocalDateTime date, BigDecimal price);
    Vehicle getVehicle(String vehicleName);
    Driver getDriver(String cpf);
    List<Maintence> getVehicleNextMaintenances(String vehicleName);
}
