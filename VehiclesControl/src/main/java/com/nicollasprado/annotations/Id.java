package com.nicollasprado.annotations;

import com.nicollasprado.enums.IdStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    IdStrategy strategy() default IdStrategy.AUTO_INCREMENT;
}
