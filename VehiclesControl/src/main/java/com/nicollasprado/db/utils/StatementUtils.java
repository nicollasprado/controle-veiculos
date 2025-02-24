package com.nicollasprado.db.utils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class StatementUtils {

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
            if(value == null){
                statement.setNull(index, Types.NULL);
            }
            else if(value instanceof Integer){
                statement.setInt(index, (Integer) value);
            }
            else if(value instanceof String){
                statement.setString(index, (String) value);
            }
            else if(value instanceof Boolean){
                statement.setBoolean(index, (Boolean) value);
            }
            else if(value instanceof Double){
                statement.setDouble(index, (Double) value);
            }
            else if(value instanceof Float){
                statement.setFloat(index, (Float) value);
            }
            else if(value instanceof LocalDateTime){
                statement.setTimestamp(index, Timestamp.valueOf((LocalDateTime) value));
            }
            else if(value instanceof LocalDate){
                statement.setDate(index, Date.valueOf((LocalDate) value));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Value could not be added to statement: " + e.getMessage());
        }
    }
}
