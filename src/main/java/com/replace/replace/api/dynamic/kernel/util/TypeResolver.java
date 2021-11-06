package com.replace.replace.api.dynamic.kernel.util;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TypeResolver {

    public static Class< ? > toWrapper( Class< ? > type ) {
        if ( !type.isPrimitive() ) {
            return type;
        }

        switch ( type.getName() ) {
            case "byte":
                return Byte.class;
            case "short":
                return Short.class;
            case "int":
                return Integer.class;
            case "long":
                return Long.class;
            case "char":
                return Character.class;
            case "boolean":
                return Boolean.class;
            default:
                return null;
        }
    }
}
