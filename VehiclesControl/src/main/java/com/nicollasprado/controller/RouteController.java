package com.nicollasprado.controller;

import com.nicollasprado.abstraction.UserInterface;
import com.nicollasprado.db.Query;
import com.nicollasprado.model.Route;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class RouteController {
    private static final Query<Route, Route> routeQuery = new Query<>(Route.class);


    public static void routeCategory(Scanner input) {
        String command = "";
        do {
            System.out.println("\n============================");
            System.out.println("| Sistema Controle de veiculos");
            System.out.println("| Rotas:\n" +
                    "0- Voltar\n" +
                    "1- Registrar nova rota\n" +
                    "2- Consultar rota especifico\n" +
                    "3- Listar rotas\n"
            );

            switch (command) {
                case "1":
                    registerRoute(input);
                    break;
                case "2":
                    getSpecificRoute(input);
                    break;
                case "3":
                    getRouteList();
                    break;
            }
        } while (!(command = input.nextLine()).equals("0"));

        UserInterface.chooseCategory(input);
    }

    private static void registerRoute(Scanner input) {
        System.out.println("\n============================");
        System.out.println("| Cadastro de Rota");
        System.out.print("Local de inicio: ");
        String startLocation = input.nextLine();
        System.out.print("Local final: ");
        String finalLocation = input.nextLine();
        System.out.print("Preço do combustivel: ");
        String fuelRequiredPrice = input.nextLine();
        System.out.print("Custos adicionais: ");
        String otherCosts = input.nextLine();

        Route newRoute = new Route(startLocation, finalLocation, new BigDecimal(fuelRequiredPrice), new BigDecimal(otherCosts));
        routeQuery.save(newRoute);

        System.out.println("Veiculo cadastrado com sucesso!");
    }

    private static void getSpecificRoute(Scanner input) {
        System.out.println("\n============================");
        System.out.println("| Consulta de Rota");
        System.out.print("Digite o id da rota: ");
        String routeId = input.nextLine();

        Optional<Route> foundRoute = routeQuery.findById(routeId);

        if (foundRoute.isEmpty()) {
            System.out.println("Rota de id: '" + routeId + "' nao encontrado.");
        } else {
            Route route = foundRoute.get();
            BigDecimal totalCost = (route.getFuelRequiredPrice().add(route.getOtherCosts()));
            System.out.println("| Dados encontrados:\n" +
                    "Local de inicio: " + route.getStartLocation() +
                    "\nLocal de finalizaçao: " + route.getFinalLocation() +
                    "\nCusto total: " + (totalCost)
            );
        }
    }

    private static void getRouteList() {
        System.out.println("\n============================");
        System.out.println("| Consulta de Rotas");

        Optional<List<Route>> foundRoutes = routeQuery.findAll();

        if (foundRoutes.isEmpty()) {
            System.out.println("Nenhuma rota encontrada!");
        } else {
            List<Route> routes = foundRoutes.get();

            for (Route route : routes) {
                BigDecimal totalCost = (route.getFuelRequiredPrice().add(route.getOtherCosts()));

                System.out.println("| ID: " + route.getId() + ":\n" +
                        "Local de inicio: " + route.getStartLocation() +
                        "\nLocal de finalizaçao: " + route.getFinalLocation() +
                        "\nCusto total: " + (totalCost)
                );
            }
        }
    }

}
