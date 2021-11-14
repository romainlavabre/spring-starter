package com.replace.replace.api.poc.kernel.exception;

import java.lang.reflect.Method;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class ToManySetterParameterException extends Throwable {

    public ToManySetterParameterException( Method method ) {
        super( "To many or not enough of parameters for method " + method.getName() + " in " + method.getDeclaringClass().getSimpleName() );
    }
}
