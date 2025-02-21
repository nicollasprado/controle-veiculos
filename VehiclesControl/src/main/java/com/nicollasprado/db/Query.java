package com.nicollasprado.db;

import com.nicollasprado.Exceptions.EntityNotFoundException;
import com.nicollasprado.abstraction.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import java.sql.*;

public class Query<T extends Entity, R> {
    private final Class<T> entityClass;
    private final Class<R> returnClass;

    // https://jdbc.postgresql.org/documentation/query/


    // BECAREFUL USING THIS! MAY BE VULNARABLE TO SQL INJECTION
    public R RawQuery(String query){
        Connection conn = this.dbConnect();

        R returnObjInstance = this.getNewReturnClassInstance();

        try{
            PreparedStatement st = conn.prepareStatement(query);

            ResultSet rs = st.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            List<String> result = new ArrayList<>();
            if(!rs.next()){
                throw new EntityNotFoundException();
            }

            for(int i=1; i <= resultSetMetaData.getColumnCount(); i++){
                int columnType = resultSetMetaData.getColumnType(i);
                String columnName = resultSetMetaData.getColumnName(i);

                Object value = this.getColumnValueWithCorrespondentType(rs, columnType, i);
                this.fillEntityInstance(returnObjInstance, value, columnName);
            }

            st.close();
            rs.close();
            conn.close();

            return returnObjInstance;
        }catch (SQLException e){
            throw new RuntimeException("Error creating statement: ", e);
        }
    }


    //public <T extends returnClass> findById(){
    //    a
    //}


    @SuppressWarnings("unchecked")
    private R getNewReturnClassInstance(){
        try{
            return returnClass.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private <X> void fillEntityInstance(R returnObj, X value, String columnName){
        try{
            Field field = returnObj.getClass().getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(returnObj, value);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
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
