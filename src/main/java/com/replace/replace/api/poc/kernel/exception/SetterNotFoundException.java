package com.replace.replace.api.poc.kernel.exception;

import com.replace.replace.api.poc.annotation.Setter;

import java.lang.reflect.Field;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class SetterNotFoundException extends Throwable {

    public SetterNotFoundException( Field field ) {
        super( "Unable to resolve setter for field " + field.getName() + " in class " + field.getDeclaringClass().getSimpleName() + ". Define setter with field annotation " + Setter.class + " ( @Setter({\"\"}) )" );
    }
}
