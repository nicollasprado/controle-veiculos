package com.nicollasprado.abstraction;

import com.nicollasprado.db.Query;
import com.nicollasprado.model.Route;
import com.nicollasprado.model.Users;
import com.nicollasprado.model.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class UserInterface{
    public static void runUserInterface(){
        Scanner input = new Scanner(System.in);

        System.out.println("============================");
        System.out.println("| Login Controle de Veiculos");
        while(!login(input)){}


        String command;
        while(!(command = input.nextLine()).equals("exit")){
            System.out.println("============================");
            System.out.println("| Sistema Controle de veiculos");
            System.out.println("| Digite um comando:" +
                    "=> Veiculos:" +
                    "1v - Cadastrar de veiculo" +
                    "2v - Ver veiculo especifico" +
                    "3v - Listar veiculos" +
                    "=> Motoristas:" +
                    "1m - Cadastrar motorista" +
                    "2m - Ver motorista especifico" +
                    "3m - Listar motoristas" +
                    "=> Rotas:" +
                    "1r - Cadastrar rota" +
                    "2r - Ver rota especifica" +
                    "3r - Listar rotas" +
                    "=> Manutençoes:" +
                    "1ma - Cadastrar manutencao" +
                    "2ma - Ver manutencao especifica" +
                    "3ma - Listar manutencoes"
            );

            switch (command){
                case "1":
                    registerVehicle(input);
                    break;
            }
        }
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

    private static void registerVehicle(Scanner input){
        System.out.println(" ");
        System.out.println("============================");
        System.out.println("| Cadastro de Veiculo");
        System.out.print("Nome do veiculo: ");
        String name = input.nextLine();

        String addRoute;
        do {
            System.out.print("Adicionar rota? (sim/nao): ");
        } while (!((addRoute = input.nextLine()).equals("nao")) && !((addRoute = input.nextLine()).equals("sim")));

        List<Route> routes = new ArrayList<>();
        if(addRoute.equals("sim")){
            System.out.println("Digite o id das rotas, em branco para parar:");

            Query<Route, Route> routeQuery = new Query<>(Route.class);

            while(input.hasNext()){
                String id = input.nextLine();
                Optional<Route> found = routeQuery.findById(id);

                if(found.isEmpty()){
                    System.out.println("Rota de id '" + id + "' nao encontrada.");
                }else {
                    routes.add(found.get());
                }
            }
        }

        Query<Vehicle, Vehicle> vehicleQuery = new Query<>(Vehicle.class);
        vehicleQuery.save(new Vehicle(name, routes));

        System.out.println("Veiculo cadastrado com sucesso!");
    }

}
