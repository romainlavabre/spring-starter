package com.replace.replace.api.poc.kernel.util;

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
            case "float":
                return Float.class;
            case "double":
                return Double.class;
            case "char":
                return Character.class;
            case "boolean":
                return Boolean.class;
            default:
                return null;
        }
    }


    public static boolean isWrapperOrPrimitive( Object object ) {
        return object.getClass().isPrimitive()
                || object.getClass().equals( Byte.class )
                || object.getClass().equals( Short.class )
                || object.getClass().equals( Integer.class )
                || object.getClass().equals( Long.class )
                || object.getClass().equals( Float.class )
                || object.getClass().equals( Double.class )
                || object.getClass().equals( Character.class )
                || object.getClass().equals( Boolean.class );
    }


    public static Object castTo( Class< ? > type, Object value ) {
        if ( value == null ) {
            return null;
        }

        if ( type == Byte.class ) {
            return Byte.valueOf( value.toString() );
        }

        if ( type == Short.class ) {
            return Short.valueOf( value.toString() );
        }

        if ( type == Integer.class ) {
            return Integer.valueOf( value.toString() );
        }

        if ( type == Long.class ) {
            return Long.valueOf( value.toString() );
        }

        if ( type == Float.class ) {
            return Float.valueOf( value.toString() );
        }

        if ( type == Double.class ) {
            return Double.valueOf( value.toString() );
        }

        if ( type == Character.class ) {
            return value.toString().charAt( 0 );
        }

        if ( type == Boolean.class ) {
            return Boolean.valueOf( value.toString() );
        }

        if ( type == String.class ) {
            return value.toString();
        }

        return value;
    }
}
