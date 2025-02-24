package com.nicollasprado.db;

import com.nicollasprado.Exceptions.EntityNotFoundException;
import com.nicollasprado.abstraction.Entity;
import com.nicollasprado.db.abstractions.ReturnClassHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import java.sql.*;

// https://jdbc.postgresql.org/documentation/query/

public class Query<T extends Entity, R> {
    private final Class<T> entityClass;
    private final Class<R> returnClass;


    // BECAREFUL USING THIS! MAY BE VULNARABLE TO SQL INJECTION
    public R RawQuery(String query){
        Connection conn = this.dbConnect();

        ReturnClassHandler<R> returnClassHandler = new ReturnClassHandler<>(returnClass);

        try{
            PreparedStatement st = conn.prepareStatement(query);

            ResultSet rs = st.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            if(!rs.next()){
                throw new EntityNotFoundException();
            }

            for(int i=1; i <= resultSetMetaData.getColumnCount(); i++){
                int columnType = resultSetMetaData.getColumnType(i);
                String columnName = resultSetMetaData.getColumnName(i);

                Object value = this.getColumnValueWithCorrespondentType(rs, columnType, i);
                returnClassHandler.fillEntityInstance(value, columnName);
            }

            st.close();
            rs.close();
            conn.close();

            return returnClassHandler.getReturnClassInstance();
        }catch (SQLException e){
            throw new RuntimeException("Error creating statement: ", e);
        }
    }

    // Not the best way to do this but the easiest to now
    private Object getColumnValueWithCorrespondentType(ResultSet queryResult, int columnType, int columnIndex){
        try{
            return switch (columnType) {
                case Types.VARCHAR -> queryResult.getString(columnIndex);
                case Types.INTEGER -> queryResult.getInt(columnIndex);
                case Types.DOUBLE -> queryResult.getDouble(columnIndex);
                case Types.FLOAT -> queryResult.getFloat(columnIndex);
                case Types.TIMESTAMP -> queryResult.getTimestamp(columnIndex);
                case Types.DATE -> queryResult.getDate(columnIndex);
                case Types.ARRAY -> queryResult.getArray(columnIndex);
                case Types.BOOLEAN -> queryResult.getBoolean(columnIndex);
                default -> null;
            };
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to get column value: ", e);
        }
    }

    private Connection dbConnect(){
        try{
            String url = "jdbc:postgresql://localhost:5432/vehicles_control";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "");

            return DriverManager.getConnection(url, props);
        }catch (SQLException e){
            throw new RuntimeException("Error connection to database: ", e);
        }
    }


    @SuppressWarnings("unchecked")
    public Query(Class<T> entityClass){
        this(entityClass, (Class<R>) entityClass);
    }

    public Query(Class<T> entityClass, Class<R> returnClass){
        this.entityClass = entityClass;
        this.returnClass = returnClass;
    }
}
