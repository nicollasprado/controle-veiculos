package com.nicollasprado;

import com.nicollasprado.db.Query;
import com.nicollasprado.model.Admin;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("SISTEMA ONLINE!");

        Admin admin = new Admin("administrador333", "asdasasdasdasd");

        Query<Admin, Admin> adminQuery = new Query<>(Admin.class);

//        adminQuery.save(admin);
        List<Admin> found = adminQuery.findAll();
        for(Admin adm : found){
            System.out.println(adm.getUsername());
        }
    }


    private static void userInterface(){
        Scanner input = new Scanner(System.in);

        while(!input.next().equals("exit")){
        }
    }
}