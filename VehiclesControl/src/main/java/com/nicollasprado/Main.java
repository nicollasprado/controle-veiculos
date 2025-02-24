package com.nicollasprado;

import com.nicollasprado.db.Query;
import com.nicollasprado.model.Teste;

public class Main {
    public static void main(String[] args) {
        System.out.println("SISTEMA ONLINE!");

        Teste teste = new Teste("teste", 200);
        Query<Teste, Teste> testeQuery = new Query<>(Teste.class);
        Teste retorno = testeQuery.RawQuery("SELECT * FROM teste WHERE num = '" + teste.getNum() + "' AND name = '" + teste.getName() + "'");

        System.out.println(retorno.getName() + " " + retorno.getId() + " " + retorno.getNum());
        System.out.println(testeQuery.findById(teste.getName()).getName());

        Teste teste2 = new Teste("teste2", 500);
        testeQuery.save(teste2);
        System.out.println(testeQuery.findById(teste2.getName()).getName());
    }
}