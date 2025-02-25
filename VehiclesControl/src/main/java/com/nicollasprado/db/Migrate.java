package com.nicollasprado.db;

import com.nicollasprado.annotations.Entity;
import com.nicollasprado.utils.AnnotationHandler;
import com.nicollasprado.utils.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

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
                    System.out.println(model.getSimpleName());
                    createTable(model);
                }

            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Model class not found: " + e.getMessage());
            }
        }
    }

    private static void createTable(Class<?> entity){
        String tableName = AnnotationHandler.getEntityName(entity);

        StringBuilder query = new StringBuilder("CREATE TABLE " + tableName + " (");

        List<Field> columnFields = AnnotationHandler.getColumnFields(entity);

        // check if have an id
        AnnotationHandler.getIdField(entity);

        for(int i=0; i < columnFields.size(); i++){
            String fieldName = columnFields.get(i).getName();
            String sqlType = EntityUtils.getSqlTypeByField(columnFields.get(i));
            String columnDetails = AnnotationHandler.getColumnDetails(columnFields.get(i));

            if(i == (columnFields.size() - 1)){
                query.append(fieldName)
                        .append(" ")
                        .append(sqlType)
                        .append(" ")
                        .append(columnDetails)
                        .append(");");
            }else{
                query.append(fieldName)
                        .append(" ")
                        .append(sqlType)
                        .append(" ")
                        .append(columnDetails)
                        .append(", ");
            }
        }

        System.out.println(query);

        try{
            Statement statement = DbConnectionHandler.db.createStatement();
            statement.executeUpdate(query.toString());
            DbConnectionHandler.db.commit();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error creating table in migration: " + e.getMessage());
        }
    }

}
