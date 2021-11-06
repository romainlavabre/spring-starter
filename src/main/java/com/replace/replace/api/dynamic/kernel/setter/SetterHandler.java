package com.replace.replace.api.dynamic.kernel.setter;

import com.replace.replace.api.dynamic.kernel.exception.InvalidSetterParameterType;
import com.replace.replace.api.dynamic.kernel.exception.MultipleSetterFoundException;
import com.replace.replace.api.dynamic.kernel.exception.SetterNotFoundException;
import com.replace.replace.api.dynamic.kernel.exception.ToManySetterParameterException;
import com.replace.replace.api.dynamic.kernel.util.Formatter;
import com.replace.replace.api.dynamic.kernel.util.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class SetterHandler {

    protected static final Map< String, Setter > storage = new HashMap<>();


    public static Setter toSetter( Field field )
            throws InvalidSetterParameterType,
                   ToManySetterParameterException,
                   MultipleSetterFoundException,
                   SetterNotFoundException,
                   NoSuchMethodException {

        String id = field.getDeclaringClass().getName() + field.getName();

        if ( storage.containsKey( id ) ) {
            return storage.get( id );
        }

        Setter setter = new Setter( field );

        storage.put( id, setter );

        return toSetter( field );
    }


    public static class Setter {

        private final Logger logger = LoggerFactory.getLogger( this.getClass() );

        private Method method;

        private boolean isArrayOrCollection;

        private final Field field;

        private boolean isComputed;


        public Setter( Field field )
                throws InvalidSetterParameterType,
                       ToManySetterParameterException,
                       MultipleSetterFoundException,
                       SetterNotFoundException,
                       NoSuchMethodException {
            this.field = field;
            compute();
        }


        private void compute()
                throws SetterNotFoundException,
                       MultipleSetterFoundException,
                       ToManySetterParameterException,
                       InvalidSetterParameterType,
                       NoSuchMethodException {
            if ( isComputed ) {
                return;
            }


            Type type = field.getType();

            if ( Collection.class.isAssignableFrom( ( Class< ? > ) type )
                    || (( Class< ? > ) type).isArray() ) {
                isArrayOrCollection = true;
            }

            method = searchSetter();

            if ( method.getParameterCount() != 1 ) {
                throw new ToManySetterParameterException( method );
            }

            if ( method.getParameterTypes()[ 0 ].isPrimitive() ) {
                logger.error( "Type parameter of setter " + method.getName() + " in " + method.getDeclaringClass().getName() + " is primitive, this could throw a NullPointerException" );
            }

            if ( !TypeResolver.toWrapper( method.getParameterTypes()[ 0 ] ).getName().equals( TypeResolver.toWrapper( ( Class< ? > ) type ).getName() ) ) {
                throw new InvalidSetterParameterType( method );
            }


            isComputed = true;
        }


        protected Method searchSetter()
                throws SetterNotFoundException, MultipleSetterFoundException, NoSuchMethodException {
            com.replace.replace.api.dynamic.annotation.Setter setter = field.getAnnotation( com.replace.replace.api.dynamic.annotation.Setter.class );

            if ( setter != null ) {
                return field.getDeclaringClass().getMethod( setter.value() );
            }

            List< String > searchs = new ArrayList<>();
            List< Method > founds  = new ArrayList<>();

            if ( isArrayOrCollection ) {
                searchs.add( "add" + Formatter.toPascalCase( field.getName() ) );
            } else {
                searchs.add( "set" + Formatter.toPascalCase( field.getName() ) );
            }

            for ( Method method : field.getDeclaringClass().getDeclaredMethods() ) {
                if ( !Modifier.isPublic( method.getModifiers() ) ) {
                    continue;
                }

                for ( String search : searchs ) {
                    if ( method.getName().equals( search ) ) {
                        founds.add( method );
                    }
                }
            }

            if ( founds.size() == 0 ) {
                throw new SetterNotFoundException( field );
            }

            if ( founds.size() > 1 ) {
                throw new MultipleSetterFoundException( field );
            }

            return founds.get( 0 );
        }
    }
}
