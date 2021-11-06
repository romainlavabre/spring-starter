package com.replace.replace.api.dynamic.kernel.util;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TypeResolver {

    public static String toWrapper( Class< ? > type ) {
        if ( !type.isPrimitive() ) {
            return type.getName();
        }

        switch ( type.getName() ) {
            case "byte":
                return Byte.class.getName();
            case "short":
                return Short.class.getName();
            case "int":
                return Integer.class.getName();
            case "long":
                return Long.class.getName();
            case "char":
                return Character.class.getName();
            default:
                return null;
        }
    }
}
