package com.nicollasprado.controller;

import com.nicollasprado.db.Query;
import com.nicollasprado.model.Driver;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class DriverController {
    private static final Query<Driver, Driver> driverQuery = new Query<>(Driver.class);


    public static void driverCategory(Scanner input){
        String command = "";
        do{
            System.out.println("\n============================");
            System.out.println("| Sistema Controle de veiculos");
            System.out.println("| Motoristas:\n" +
                    "0- Sair\n" +
                    "1- Registrar novo motorista\n" +
                    "2- Consultar motorista especifico\n" +
                    "3- Listar motoristas\n"
            );

            switch (command) {
                case "1":
                    registerDriver(input);
                    break;
                case "2":
                    getSpecificDriver(input);
                    break;
                case "3":
                    getDriverList();
                    break;
            }
        } while(!(command = input.nextLine()).equals("0"));
    }

    private static void registerDriver(Scanner input){
        System.out.println("\n============================");
        System.out.println("| Cadastro de Motorista");
        System.out.print("Nome do motorista: ");
        String name = input.nextLine();
        System.out.print("CPF do motorista: ");
        String cpf = input.nextLine();

        Driver newDriver = new Driver(name, cpf);
        driverQuery.save(newDriver);
        System.out.println("Motorista cadastrado com sucesso!");
    }

    private static void getSpecificDriver(Scanner input){
        System.out.println("\n============================");
        System.out.println("| Consulta de Motorista");
        System.out.print("Digite o cpf do motorista: ");
        String driverCpf = input.nextLine();

        Optional<Driver> foundDriver = driverQuery.findBy(List.of("cpf"), List.of(driverCpf));
        if(foundDriver.isEmpty()){
            System.out.println("Motorista de cpf: '" + driverCpf + "' nao encontrado.");
        }else{
            Driver driver = foundDriver.get();
            System.out.println("| Dados encontrados:\n" +
                    "ID: " + driver.getId() +
                    "\nCpf: " + driver.getCpf() +
                    "\nNome: " + driver.getName()
            );
        }
    }

    private static void getDriverList(){
        System.out.println("\n============================");
        System.out.println("| Consulta de Motoristas");

        Optional<List<Driver>> foundDriver = driverQuery.findAll();

        if(foundDriver.isEmpty()){
            System.out.println("Nenhum motorista encontrado!");
        }else{
            List<Driver> drivers = foundDriver.get();

            for(Driver driver : drivers){
                System.out.println("| " + driver.getName() + ":\n" +
                        "ID: " + driver.getId() +
                        "\nCpf: " + driver.getCpf()
                );
            }
        }
    }
}
