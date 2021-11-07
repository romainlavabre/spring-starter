package com.replace.replace.api.dynamic.kernel.setter;

import com.replace.replace.api.dynamic.annotation.RequestParameter;
import com.replace.replace.api.dynamic.kernel.entity.EntityHandler;
import com.replace.replace.api.dynamic.kernel.exception.InvalidSetterParameterType;
import com.replace.replace.api.dynamic.kernel.exception.MultipleSetterFoundException;
import com.replace.replace.api.dynamic.kernel.exception.SetterNotFoundException;
import com.replace.replace.api.dynamic.kernel.exception.ToManySetterParameterException;
import com.replace.replace.api.dynamic.kernel.util.Formatter;
import com.replace.replace.api.dynamic.kernel.util.TypeResolver;
import com.replace.replace.api.request.Request;
import com.replace.replace.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.*;
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

        private String requestParameter;

        private Method method;

        private Class< ? > methodParameter;

        private boolean isArrayOrCollection;

        private boolean isRelation;

        private EntityHandler.Entity relationType;

        private Class< ? > genericType;

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


        public void invoke( Request request, Object entity )
                throws InvocationTargetException, IllegalAccessException {

            logger.debug( "Search in request parameter \"" + requestParameter + "\"" );

            if ( isArrayOrCollection ) {
                if ( Collection.class.isAssignableFrom( field.getType() ) ) {
                    Collection collection = ( Collection ) field.get( entity );

                    if ( collection != null ) {
                        collection.clear();
                    }
                }
            }

            if ( isArrayOrCollection && !isRelation ) {
                List< Object > values = request.getParameters( requestParameter );

                if ( values == null ) {
                    method.invoke( entity, TypeResolver.castTo( genericType, null ) );
                    return;
                }

                for ( Object value : values ) {
                    method.invoke( entity, TypeResolver.castTo( genericType, value ) );
                }

                return;
            } else if ( isRelation && !isArrayOrCollection ) {
                Long value = ( Long ) TypeResolver.castTo( Long.class, request.getParameter( requestParameter ) );

                DefaultRepository defaultRepository = EntityHandler.getEntity( relationType.getSubject() ).getDefaultRepository();

                method.invoke( entity, defaultRepository.findOrFail( value ) );
                return;
            } else if ( isArrayOrCollection ) {
                List< Object > values = request.getParameters( requestParameter );

                if ( values == null ) {
                    method.invoke( entity, TypeResolver.castTo( genericType, null ) );
                    return;
                }

                DefaultRepository defaultRepository = EntityHandler.getEntity( relationType.getSubject() ).getDefaultRepository();

                for ( Object value : values ) {
                    Long castedValue = ( Long ) TypeResolver.castTo( Long.class, value );
                    method.invoke( entity, defaultRepository.findOrFail( castedValue ) );
                }

                return;
            }

            Object value = TypeResolver.castTo( methodParameter, request.getParameter( requestParameter ) );


            method.invoke( entity, value );
        }


        public Method getMethod() {
            return method;
        }


        public Field getField() {
            return field;
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

            if ( field.isAnnotationPresent( OneToOne.class )
                    || field.isAnnotationPresent( OneToMany.class )
                    || field.isAnnotationPresent( ManyToOne.class )
                    || field.isAnnotationPresent( ManyToMany.class ) ) {
                isRelation = true;

                if ( !isArrayOrCollection ) {
                    relationType = EntityHandler.getEntity( field.getType() );
                } else {
                    ParameterizedType parameterizedType = ( ParameterizedType ) field.getGenericType();
                    relationType = EntityHandler.getEntity( ( Class< ? > ) parameterizedType.getActualTypeArguments()[ 0 ] );
                }
            }

            if ( isArrayOrCollection ) {
                ParameterizedType parameterizedType = ( ParameterizedType ) field.getGenericType();
                genericType = ( Class< ? > ) parameterizedType.getActualTypeArguments()[ 0 ];
            }

            method = searchSetter();

            if ( method.getParameterCount() != 1 ) {
                throw new ToManySetterParameterException( method );
            }

            if ( method.getParameterTypes()[ 0 ].isPrimitive() ) {
                logger.error( "Type parameter of setter " + method.getName() + " in " + method.getDeclaringClass().getName() + " is primitive, this could throw a NullPointerException" );
            }

            methodParameter = TypeResolver.toWrapper( method.getParameterTypes()[ 0 ] );

            if ( !isArrayOrCollection
                    && !methodParameter.getName().equals( TypeResolver.toWrapper( ( Class< ? > ) type ).getName() ) ) {
                throw new InvalidSetterParameterType( method );
            }

            RequestParameter requestParameter = field.getAnnotation( RequestParameter.class );

            if ( requestParameter != null ) {
                this.requestParameter = requestParameter.name();
            } else {
                if ( !isRelation ) {
                    this.requestParameter = Formatter.toSnakeCase( field.getDeclaringClass().getSimpleName() + "_" + field.getName() );
                } else {
                    this.requestParameter = Formatter.toSnakeCase( field.getDeclaringClass().getSimpleName() + "_" + field.getName() + "_id" );
                }
            }

            isComputed = true;
        }


        protected Method searchSetter()
                throws SetterNotFoundException, MultipleSetterFoundException, NoSuchMethodException {
            com.replace.replace.api.dynamic.annotation.Setter setter = field.getAnnotation( com.replace.replace.api.dynamic.annotation.Setter.class );


            List< String > searchs = new ArrayList<>();
            List< Method > founds  = new ArrayList<>();

            if ( isArrayOrCollection ) {
                searchs.add( "add" + Formatter.toPascalCase( field.getName() ) );
            } else {
                searchs.add( "set" + Formatter.toPascalCase( field.getName() ) );
            }

            if ( setter != null ) {
                searchs.add( setter.value() );
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
