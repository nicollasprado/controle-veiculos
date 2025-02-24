package com.nicollasprado.db.utils;

import com.nicollasprado.Exceptions.EntityIdNotFoundException;
import com.nicollasprado.db.annotations.Entity;
import com.nicollasprado.db.annotations.Id;

import java.lang.reflect.Field;

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

}
