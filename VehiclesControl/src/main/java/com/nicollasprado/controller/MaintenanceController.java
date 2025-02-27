package com.nicollasprado.controller;

import com.nicollasprado.abstraction.UserInterface;
import com.nicollasprado.db.Query;
import com.nicollasprado.model.Maintenance;
import com.nicollasprado.model.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class MaintenanceController {
    private static final Query<Maintenance, Maintenance> maintenanceQuery = new Query<>(Maintenance.class);
    private static final Query<Vehicle, Vehicle> vehicleQuery = new Query<>(Vehicle.class);


    public static void maintenanceCategory(Scanner input){
        String command = "";
        do{
            System.out.println("\n============================");
            System.out.println("| Sistema Controle de veiculos");
            System.out.println("| Manutençoes:\n" +
                    "0- Voltar\n" +
                    "1- Registrar nova manutençao\n" +
                    "2- Consultar manutençao especifico\n" +
                    "3- Listar manutençoes\n"
            );

            switch (command) {
                case "1":
                    registerMaintenance(input);
                    break;
                case "2":
                    getSpecificMaintenance(input);
                    break;
                case "3":
                    getMaintenanceList();
                    break;
            }
        } while(!(command = input.nextLine()).equals("0"));

        UserInterface.chooseCategory(input);
    }

    private static void registerMaintenance(Scanner input){
        System.out.println("\n============================");
        System.out.println("| Cadastro de Manutençao");

        Optional<Vehicle> foundVehicle = Optional.empty();
        String vehicleName = "";
        while(foundVehicle.isEmpty()){
            System.out.print("Nome do veiculo: ");
            vehicleName = input.nextLine();

            foundVehicle = vehicleQuery.findBy(List.of("name"), List.of(vehicleName));
            if(foundVehicle.isEmpty()){
                System.out.println("Veiculo invalido");
            }
        }

        System.out.print("Data da manutençao (DD/MM/YYYY): ");
        String date = input.nextLine();
        System.out.print("Horario da manutençao (HH:MM): ");
        String time = input.nextLine();
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String dateTime = date + " " + time + ":00";
            LocalDateTime finalDateTime = LocalDateTime.parse(dateTime, formatter);

            System.out.print("Preço da manutençao: ");
            String price = input.nextLine();

            Maintenance newMaintenance = new Maintenance(vehicleName, finalDateTime, new BigDecimal(price));
            maintenanceQuery.save(newMaintenance);
            System.out.println("Manutençao cadastrada com sucesso!");
        }catch (DateTimeParseException e){
            System.out.println("Data ou horario invalido.");
        }
    }

    private static void getSpecificMaintenance(Scanner input){
        System.out.println("\n============================");
        System.out.println("| Consulta de Manutençao");
        System.out.print("Digite o id da manutençao: ");
        String maintenanceId = input.nextLine();

        Optional<Maintenance> foundMaintenance = maintenanceQuery.findById(maintenanceId);
        if(foundMaintenance.isEmpty()){
            System.out.println("Manutençao de id: '" + maintenanceId + "' nao encontrado.");
        }else{
            Maintenance maintenance = foundMaintenance.get();
            System.out.println("| Dados encontrados:\n" +
                    "Veiculo: " + maintenance.getVehicleName() +
                    "\nData: " + maintenance.getDateTime() +
                    "\nPreço: " + maintenance.getPrice()
            );
        }
    }

    private static void getMaintenanceList(){
        System.out.println("\n============================");
        System.out.println("| Consulta de Manutençoes");

        Optional<List<Maintenance>> foundMaintenances = maintenanceQuery.findAll();

        if(foundMaintenances.isEmpty()){
            System.out.println("Nenhuma manutençao encontrada!");
        }else{
            List<Maintenance> maintenances = foundMaintenances.get();

            for(Maintenance maintenance : maintenances){
                System.out.println("| Id: " + maintenance.getId() +
                        "\nVeiculo: " + maintenance.getVehicleName() +
                        "\nData: " + maintenance.getDateTime() +
                        "\nPreço: " + maintenance.getPrice()
                );
            }
        }
    }
}
