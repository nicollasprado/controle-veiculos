package com.nicollasprado;

import com.nicollasprado.model.Teste;

public class Main {
    public static void main(String[] args) {
        System.out.println("SISTEMA ONLINE!");

        Teste teste = new Teste("teste", 200);

        System.out.println(ur.getById(teste.getName()));
    }
}