package com.nicollasprado.db;

import com.nicollasprado.db.annotations.Entity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Migrate {
    public static void main(String[] args) {
        String currentDir = new File("").getAbsolutePath();
        Path modelsDir = Paths.get(currentDir + "/VehiclesControl/src/main/java/com/nicollasprado/model");

        try{
            Files.walk(modelsDir).forEach(path -> iterateFiles(path.toFile()));
        } catch (IOException e) {
            throw new RuntimeException("Invalid models path: " + e.getMessage());
        }
    }


    private static void iterateFiles(File file){
        if(file.isFile()){
            String className = file.getName().replace(".java", "");

            try{
                Class<?> model = Class.forName("com.nicollasprado.model." + className);
                if(model.isAnnotationPresent(Entity.class)){
                    // TODO - FINALIZE THIS MIGRATE FUNCTION + CREATE ANNOTATIONS TO NOT NULL ETC
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Model class not found: " + e.getMessage());
            }
        }
    }
}
