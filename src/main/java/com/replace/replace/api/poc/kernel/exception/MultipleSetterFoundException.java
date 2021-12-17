package com.replace.replace.api.poc.kernel.exception;

import com.replace.replace.api.poc.annotation.Setter;

import java.lang.reflect.Field;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class MultipleSetterFoundException extends Throwable {

    public MultipleSetterFoundException( Field field ) {
        super( "Multiple setter found for " + field.getName() + " in " + field.getDeclaringClass().getSimpleName() + ". Define setter with field annotation " + Setter.class + " ( @Setter({\"\"}) )" );
    }
}
