package com.nicollasprado.db;

import com.nicollasprado.annotations.Entity;
import com.nicollasprado.enums.MigrateTableExistConfig;
import com.nicollasprado.utils.AnnotationHandler;
import com.nicollasprado.utils.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public abstract class Migrate {
    private static final MigrateTableExistConfig TABLE_EXISTENCE = MigrateTableExistConfig.DROP_AND_CREATE_NEW;

    public static void main(String[] args) {
        String currentDir = new File("").getAbsolutePath();
        Path modelsDir = Paths.get(currentDir + "/VehiclesControl/src/main/java/com/nicollasprado/model");

        try{
            Scanner input = new Scanner(System.in);
            System.out.print("Digite o nome da migration: ");
            String migrationName = input.nextLine();
            migrationName = migrationName.replaceAll("\\s", "_");

            LocalDateTime dateTime = LocalDateTime.now();
            Path migrationFilePath = Path.of(new File("").getAbsolutePath() + "/VehiclesControl/src/main/java/com/nicollasprado/db/migrations/" +
                    dateTime.getYear() +
                    dateTime.getDayOfMonth() +
                    dateTime.getMonthValue() + "-" +
                    dateTime.getHour() +
                    dateTime.getMinute() + "-" +
                    migrationName
                    + ".txt"
            );

            Files.walk(modelsDir).forEach(path -> iterateFiles(path.toFile(), migrationFilePath));
        } catch (IOException e) {
            throw new RuntimeException("Invalid models path: " + e.getMessage());
        }
    }


    private static void iterateFiles(File file, Path migrationFilePath){
        if(file.isFile()){
            String className = file.getName().replace(".java", "");

            try{
                Class<?> model = Class.forName("com.nicollasprado.model." + className);
                if(model.isAnnotationPresent(Entity.class)){
                    createTable(model);
                    createSchemaFile(model, migrationFilePath);
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
            String fieldName = EntityUtils.getColumnFieldName(columnFields.get(i));
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

        String sqlStatus = sendRequest(query.toString());
        // table already exists
        if(sqlStatus.equals("42P07")){
            switch(Migrate.TABLE_EXISTENCE){
                case UPDATE:
                    handleTableUpdate();
                    break;
                case CREATE_NEW:
                    System.out.println("TODO - create new");
                    break;
                case DROP_AND_CREATE_NEW:
                    handleDropAndCreateNewTable(tableName, query.toString());
            }

        }else if(sqlStatus.isBlank()){
            // do nothing
        } else{
            throw new RuntimeException("Error creating table in migration, SQL STATUS " + sqlStatus);
        }
    }


    private static void handleDropAndCreateNewTable(String tableName, String query){
        sendRequest("drop table if exists " + tableName + " cascade;");
        sendRequest(query);
    }

    private static void handleCreateNewTable(String tableName){
        // alter actual table name
    }


    private static void handleTableUpdate(){
        System.out.println("todo handle update table");
    }


    // return SQL error status
    private static String sendRequest(String query) {
        Statement statement = null;
        try{
            statement = DbConnectionHandler.db.createStatement();
            statement.executeUpdate(query);
            DbConnectionHandler.db.commit();

            return "";
        }catch (SQLException e){
            try{
                DbConnectionHandler.db.rollback();
            } catch (SQLException ex) {
                e.addSuppressed(ex);
            }
            return e.getSQLState();
        }finally {
            if(statement != null){
                try{
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Error closing statement: " + e.getMessage());
                }
            }
        }
    }


    private static void createSchemaFile(Class<?> entity, Path migrationFilePath){
        try{
            if(Files.exists(migrationFilePath)){
                Files.writeString(migrationFilePath, "\n", StandardOpenOption.APPEND);
            }else{
                Files.createFile(migrationFilePath);
            }


            Files.writeString(migrationFilePath, "TABLE " + AnnotationHandler.getEntityName(entity) + "\n", StandardOpenOption.APPEND);

            List<Field> columnFields = AnnotationHandler.getColumnFields(entity);
            for (Field columnField : columnFields) {
                String fieldName = EntityUtils.getColumnFieldName(columnField);
                String sqlType = EntityUtils.getSqlTypeByField(columnField);
                String columnDetails = AnnotationHandler.getColumnDetails(columnField);

                String column = "COLUMN " + fieldName + " " + sqlType + " " + columnDetails + "\n";

                Files.writeString(migrationFilePath, column, StandardOpenOption.APPEND);
            }

        } catch (IOException e) {
            throw new RuntimeException("Error creating migration file: " + e);
        }
    }

}
