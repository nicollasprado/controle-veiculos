package com.nicollasprado.db.abstractions;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

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

    public <X> void fillEntityInstance(X value, String columnName){
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
