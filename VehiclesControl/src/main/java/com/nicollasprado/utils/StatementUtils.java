package com.nicollasprado.utils;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Id;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StatementUtils {

    public static <X> List<Object> getValidParamsNoId(List<Field> columnFields, X entity){
        List<Object> params = new ArrayList<>();
        for(Field field : columnFields){
            if(field.isAnnotationPresent(Column.class) && !field.isAnnotationPresent(Id.class)){
                params.add(EntityUtils.getColumnValue(field, entity));
            }
        }

        return params;
    }

    public static String prepareRefinedQueryNoId(List<Field> columnFields, String entityName){
        StringBuilder columnsNames = new StringBuilder("(");

        // starts from 1 to ignore id
        for(int i = 1; i < columnFields.size(); i++){
            if(i == columnFields.size() - 1){
                columnsNames.append(EntityUtils.getColumnFieldName(columnFields.get(i))).append(")");
            }else{
                columnsNames.append(EntityUtils.getColumnFieldName(columnFields.get(i))).append(", ");
            }
        }

        StringBuilder paramsEntries = new StringBuilder("(");
        // starts from 1 to ignore id
        for(int i = 1; i < columnFields.size(); i++){
            if(i == columnFields.size() - 1){
                paramsEntries.append("?)");
            }else{
                paramsEntries.append("?, ");
            }
        }

        return ("INSERT INTO " + entityName + " " + columnsNames + " VALUES " + paramsEntries + ";");
    }

    public static PreparedStatement getValidStatement(Connection conn, String query, List<?> parameters){
        try{
            PreparedStatement statement = conn.prepareStatement(query);

            for(int i = 1; i <= parameters.size(); i++){
                StatementUtils.addStatementValue(statement, i, parameters.get(i-1));
            }

            return statement;
        } catch (SQLException e) {
            throw new RuntimeException("Error while preparing refined statement: " + e);
        }

    }

    private static <X> void addStatementValue(PreparedStatement statement, int index, X value){
        try{
            switch (value) {
                case null -> statement.setNull(index, Types.NULL);
                case Integer i -> statement.setInt(index, i);
                case String s -> statement.setString(index, s);
                case Boolean b -> statement.setBoolean(index, b);
                case Double v -> statement.setDouble(index, v);
                case Float v -> statement.setFloat(index, v);
                case LocalDateTime localDateTime -> statement.setTimestamp(index, Timestamp.valueOf(localDateTime));
                case LocalDate localDate -> statement.setDate(index, Date.valueOf(localDate));
                case UUID uuid -> statement.setObject(index, uuid, Types.OTHER);
                default -> {
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Value could not be added to statement: " + e.getMessage());
        }
    }
}
