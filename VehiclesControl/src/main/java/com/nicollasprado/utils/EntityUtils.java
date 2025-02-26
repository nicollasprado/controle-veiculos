package com.nicollasprado.utils;

import com.nicollasprado.annotations.Column;
import com.nicollasprado.annotations.Id;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.UUID;

public class EntityUtils {

    public static String getColumnFieldName(Field field){
        String columnName;
        if(field.isAnnotationPresent(Column.class)){
            columnName = field.getAnnotation(Column.class).name();
        }else{
            columnName = field.getAnnotation(Id.class).name();
        }

        return columnName.isEmpty() ? field.getName().toLowerCase() : columnName;
    }

    public static <T> Object getColumnValue(Field field, T entity){
        field.setAccessible(true);

        try{
            return field.get(entity);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Erro while getting column value: " + e.getMessage());
        }
    }

    // Not the best way to do this but the easiest for now
    public static Object getColumnValueWithCorrespondentType(ResultSet queryResult, int columnType, int columnIndex){
        try{
            return switch (columnType) {
                case Types.VARCHAR -> queryResult.getString(columnIndex);
                case Types.INTEGER -> queryResult.getInt(columnIndex);
                case Types.DOUBLE -> queryResult.getDouble(columnIndex);
                case Types.FLOAT -> queryResult.getFloat(columnIndex);
                case Types.TIMESTAMP -> queryResult.getTimestamp(columnIndex);
                case Types.DATE -> queryResult.getDate(columnIndex);
                case Types.BOOLEAN -> queryResult.getBoolean(columnIndex);
                case Types.SMALLINT -> queryResult.getShort(columnIndex);
                case Types.TINYINT -> queryResult.getByte(columnIndex);
                case Types.OTHER -> queryResult.getObject(columnIndex);
                default -> null;
            };
        } catch (SQLException e) {
            throw new RuntimeException("Error while trying to get column value: ", e);
        }
    }

    public static String getSqlTypeByField(Field field){
        Class<?> fieldType = field.getType();

        if (fieldType == int.class || fieldType == Integer.class) {
            return "INTEGER";
        } else if (fieldType == long.class || fieldType == Long.class) {
            return "BIGINT";
        } else if (fieldType == double.class || fieldType == Double.class) {
            return "DOUBLE";
        } else if (fieldType == float.class || fieldType == Float.class) {
            return "FLOAT";
        } else if (fieldType == short.class || fieldType == Short.class) {
            return "SMALLINT";
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return "TINYINT";
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return "BOOLEAN";
        } else if (fieldType == String.class) {
            return "VARCHAR";
        } else if (fieldType == java.util.Date.class || fieldType == java.sql.Date.class) {
            return "DATE";
        } else if (fieldType == java.sql.Timestamp.class || fieldType == LocalDateTime.class) {
            return "TIMESTAMP";
        } else if(fieldType == BigDecimal.class){
            return "DECIMAL";
        } else if (fieldType == UUID.class) {
            return "UUID";
        }

        throw new IllegalArgumentException("Unsuported data type: " + fieldType.getName());
    }

}
