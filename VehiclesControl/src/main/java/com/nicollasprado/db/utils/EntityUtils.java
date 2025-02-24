package com.nicollasprado.db.utils;

import com.nicollasprado.Exceptions.EntityIdNotFoundException;
import com.nicollasprado.db.annotations.Column;
import com.nicollasprado.db.annotations.Entity;
import com.nicollasprado.db.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtils {

    public static Field getIdField(Class<?> target){
        for(Field field : target.getDeclaredFields()){
            if(field.isAnnotationPresent(Id.class)){
                return field;
            }
        }

        throw new EntityIdNotFoundException();
    }

    public static String getEntityName(Class<?> target){
        String entityName = target.getSimpleName().toLowerCase();

        if(target.isAnnotationPresent(Entity.class)){
            entityName = target.getAnnotation(Entity.class).name();
        }

        return entityName;
    }

    public static List<Field> getColumnFields(Class<?> target){
        return Arrays.stream(target.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Column.class))
                .collect(Collectors.toList());
    }

    public static String getColumnFieldName(Field field){
        String columnName = field.getAnnotation(Column.class).name();
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
}
