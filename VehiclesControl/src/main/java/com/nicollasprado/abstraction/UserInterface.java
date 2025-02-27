package com.nicollasprado.abstraction;

import com.nicollasprado.controller.DriverController;
import com.nicollasprado.controller.MaintenanceController;
import com.nicollasprado.controller.RouteController;
import com.nicollasprado.controller.VehicleController;
import com.nicollasprado.db.Query;
import com.nicollasprado.model.Driver;
import com.nicollasprado.model.Users;

import java.util.*;

public abstract class UserInterface{
    private final Query<Driver, Driver> driverQuery = new Query<>(Driver.class);

    public static void runUserInterface(){
        Scanner input = new Scanner(System.in);

        System.out.println("============================");
        System.out.println("| Login Controle de Veiculos");
        while(!login(input)){}

        chooseCategory(input);
    }

    private static boolean login(Scanner input) {
        System.out.print("Nome de usuario: ");
        String username = input.nextLine();
        System.out.print("Senha: ");
        String password = input.nextLine();

        Query<Users, Users> adminQuery = new Query<>(Users.class);
        Optional<Users> found = adminQuery.findBy(List.of("username", "password"), List.of(username, password));

        if(found.isEmpty()){
            System.out.println(" ");
            System.out.println("Usuario ou senha invalido, tente novamente:");
        }

        return found.isPresent();
    }

    public static void chooseCategory(Scanner input){
        String command = "";
        do{
            System.out.println("\n============================");
            System.out.println("| Sistema Controle de veiculos");
            System.out.println("| Selecione uma categoria:\n" +
                    "0- Sair\n" +
                    "1- Veiculos\n" +
                    "2- Motoristas\n" +
                    "3- Rotas\n" +
                    "4- Manutençoes"
            );

            switch (command){
                case "1":
                    VehicleController.vehiclesCategory(input);
                    break;
                case "2":
                    DriverController.driverCategory(input);
                case "3":
                    RouteController.routeCategory(input);
                case "4":
                    MaintenanceController.maintenanceCategory(input);
            }
        } while(!(command = input.nextLine()).equals("0"));
    }
}
