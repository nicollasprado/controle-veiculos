package com.nicollasprado;

import com.nicollasprado.db.Query;
import com.nicollasprado.dto.TesteDTO;
import com.nicollasprado.model.Teste;

public class Main {
    public static void main(String[] args) {
        System.out.println("SISTEMA ONLINE!");

        Teste teste = new Teste("teste", 200);
        Query<Teste, TesteDTO> testeQuery = new Query<>(Teste.class, TesteDTO.class);
        TesteDTO retorno = testeQuery.RawQuery("SELECT * FROM teste WHERE num = '" + teste.getNum() + "' AND name = '" + teste.getName() + "'");

        System.out.println(retorno.name());
    }
}