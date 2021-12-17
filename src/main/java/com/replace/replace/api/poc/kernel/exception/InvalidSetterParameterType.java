package com.replace.replace.api.poc.kernel.exception;

import java.lang.reflect.Method;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class InvalidSetterParameterType extends Throwable {
    public InvalidSetterParameterType( Method method ) {
        super( "No match parameter type for method " + method.getName() + " in " + method.getDeclaringClass().getSimpleName() );
    }
}
