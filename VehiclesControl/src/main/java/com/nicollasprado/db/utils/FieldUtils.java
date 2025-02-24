package com.nicollasprado.db.utils;

import com.nicollasprado.Exceptions.EntityIdNotFoundException;
import com.nicollasprado.db.annotations.Id;

import java.lang.reflect.Field;

public class FieldUtils {

    public static Field findIdField(Class<?> target){
        for(Field field : target.getDeclaredFields()){
            if(field.isAnnotationPresent(Id.class)){
                return field;
            }
        }

        throw new EntityIdNotFoundException();
    }

}
