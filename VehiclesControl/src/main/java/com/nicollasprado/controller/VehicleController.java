package com.nicollasprado.controller;

import com.nicollasprado.db.Query;
import com.nicollasprado.model.Route;
import com.nicollasprado.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class VehicleController {
    private static final Query<Route, Route> routeQuery = new Query<>(Route.class);
    private static final Query<Vehicle, Vehicle> vehicleQuery = new Query<>(Vehicle.class);


    public static void vehiclesCategory(Scanner input){
        String command = "";
        do{
            System.out.println("\n============================");
            System.out.println("| Sistema Controle de veiculos");
            System.out.println("| Veiculos:\n" +
                    "0- Sair\n" +
                    "1- Registrar novo veiculo\n" +
                    "2- Consultar veiculo especifico\n" +
                    "3- Listar veiculos\n"
            );

            switch (command) {
                case "1":
                    registerVehicle(input);
                    break;
                case "2":
                    getSpecificVehicle(input);
                    break;
                case "3":
                    getVehicleList();
                    break;
            }
        } while(!(command = input.nextLine()).equals("0"));
    }

    private static void registerVehicle(Scanner input){
        System.out.println("\n============================");
        System.out.println("| Cadastro de Veiculo");
        System.out.print("Nome do veiculo: ");
        String name = input.nextLine();

        String addRoute;
        do {
            System.out.print("Adicionar rota? (sim/nao): ");
            addRoute = input.nextLine();
            if(addRoute.equals("nao")) break;
        } while (!addRoute.equals("sim"));

        List<Route> routes = new ArrayList<>();
        if(addRoute.equals("sim")){
            System.out.println("Digite o id das rotas, em branco para parar:");

            while(input.hasNext()){
                String id = input.nextLine();
                try{
                    Optional<Route> found = routeQuery.findById(id);

                    if(found.isEmpty()){
                        System.out.println("Rota de id '" + id + "' nao encontrada.");
                    }else {
                        routes.add(found.get());
                    }

                }catch (IllegalArgumentException e){
                    System.err.println("UUID invalido: " + id);
                }
            }
        }


        vehicleQuery.save(new Vehicle(name, routes));

        System.out.println("Veiculo cadastrado com sucesso!");
    }

    private static void getSpecificVehicle(Scanner input){
        System.out.println("\n============================");
        System.out.println("| Consulta de Veiculo");
        System.out.print("Digite o nome do veiculo: ");
        String vehicleName = input.nextLine();

        Optional<Vehicle> foundVehicle = vehicleQuery.findBy(List.of("name"), List.of(vehicleName));

        if(foundVehicle.isEmpty()){
            System.out.println("Veiculo '" + vehicleName + "' nao encontrado.");
        }else{
            Vehicle vehicle = foundVehicle.get();
            System.out.println("| Dados encontrados:\n" +
                    "ID: " + vehicle.getId() +
                    "\nNome: " + vehicle.getName() +
                    "\nProxima manutençao: TODO" +
                    "\nRotas: TODO"
            );
        }
    }

    private static void getVehicleList(){
        System.out.println("\n============================");
        System.out.println("| Consulta de Veiculos");

        Optional<List<Vehicle>> foundVehicles = vehicleQuery.findAll();

        if(foundVehicles.isEmpty()){
            System.out.println("Nenhum veiculo encontrado!");
        }else{
            List<Vehicle> vehicles = foundVehicles.get();

            for(Vehicle vehicle : vehicles){
                System.out.println("| " + vehicle.getName() + ":\n" +
                        "ID: " + vehicle.getId() +
                        "\nProxima manutençao: TODO" +
                        "\nRotas: TODO"
                );
            }
        }
    }
}
