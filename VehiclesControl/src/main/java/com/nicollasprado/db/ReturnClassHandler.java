package com.nicollasprado.db;

import com.nicollasprado.Exceptions.EntityNotFoundException;
import com.nicollasprado.utils.EntityUtils;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ReturnClassHandler<R> {
    private R returnClassInstance;
    private final List<R> returnClassList;
    private final Class<R> returnClass;

    private R getNewReturnClassInstance(Class<R> returnClass){
        try{
            return returnClass.getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void databaseDataToEntityInstance(ResultSet resultSet) {
        try {
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                int columnType = resultSetMetaData.getColumnType(i);
                String columnName = resultSetMetaData.getColumnName(i);

                Object value = EntityUtils.getColumnValueWithCorrespondentType(resultSet, columnType, i);
                this.fillEntityInstance(value, columnName);
            }

        }catch (SQLException e) {
            throw new RuntimeException("Error converting database data to entity instance: " + e);
        }
    }

    private <X> void fillEntityInstance(X value, String columnName){
        try{
            for(Field field : returnClass.getDeclaredFields()){
                if(EntityUtils.getColumnFieldName(field).equals(columnName)){
                    field.setAccessible(true);
                    field.set(returnClassInstance, value);
                    break;
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void resolveMany(ResultSet resultSet){
        try{
            boolean first = true;

            while(resultSet.next()){
                first = false;
                databaseDataToEntityInstance(resultSet);
                returnClassList.add(this.returnClassInstance);
                this.returnClassInstance = getNewReturnClassInstance(returnClass);
            }

            if(first){
                throw new EntityNotFoundException();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while resolving many return classes: " + e);
        }
    }


    public ReturnClassHandler(Class<R> returnClass){
        this.returnClassInstance = this.getNewReturnClassInstance(returnClass);
        this.returnClassList = new ArrayList<>();
        this.returnClass = returnClass;
    }
}
