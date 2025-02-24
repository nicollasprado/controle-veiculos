package com.nicollasprado.db;

import com.nicollasprado.Exceptions.EntityNotFoundException;
import com.nicollasprado.db.utils.ColumnUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Getter
public class ReturnClassHandler<R> {
    private final R returnClassInstance;

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

            if (!resultSet.next()) {
                throw new EntityNotFoundException();
            }

            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                int columnType = resultSetMetaData.getColumnType(i);
                String columnName = resultSetMetaData.getColumnName(i);

                Object value = ColumnUtil.getColumnValueWithCorrespondentType(resultSet, columnType, i);
                this.fillEntityInstance(value, columnName);

            }
        }catch (SQLException e) {
            throw new RuntimeException("Error converting database data to entity instance: " + e);
        }
    }

    private <X> void fillEntityInstance(X value, String columnName){
        try{
            Field field = returnClassInstance.getClass().getDeclaredField(columnName);
            field.setAccessible(true);
            field.set(returnClassInstance, value);
        } catch (NoSuchFieldException ignored) {
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


    public ReturnClassHandler(Class<R> returnClass){
        this.returnClassInstance = this.getNewReturnClassInstance(returnClass);
    }
}
