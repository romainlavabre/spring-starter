package com.replace.replace.api.poc.kernel.setter;

import com.replace.replace.api.poc.annotation.Constraint;
import com.replace.replace.api.poc.annotation.RequestParameter;
import com.replace.replace.api.poc.api.CustomConstraint;
import com.replace.replace.api.poc.kernel.entity.EntityHandler;
import com.replace.replace.api.poc.kernel.exception.InvalidSetterParameterType;
import com.replace.replace.api.poc.kernel.exception.MultipleSetterFoundException;
import com.replace.replace.api.poc.kernel.exception.SetterNotFoundException;
import com.replace.replace.api.poc.kernel.exception.ToManySetterParameterException;
import com.replace.replace.api.poc.kernel.util.Formatter;
import com.replace.replace.api.poc.kernel.util.TypeResolver;
import com.replace.replace.api.request.Request;
import com.replace.replace.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SetterHandler {

    protected static final Map< String, Setter > storage = new HashMap<>();

    protected final ApplicationContext applicationContext;
    protected final EntityHandler      entityHandler;


    public SetterHandler(
            ApplicationContext applicationContext,
            EntityHandler entityHandler ) {
        this.applicationContext = applicationContext;
        this.entityHandler      = entityHandler;
    }


    public Setter toSetter( Field field )
            throws InvalidSetterParameterType,
                   ToManySetterParameterException,
                   MultipleSetterFoundException,
                   SetterNotFoundException,
                   NoSuchMethodException {

        String id = field.getDeclaringClass().getName() + field.getName();

        if ( storage.containsKey( id ) ) {
            return storage.get( id );
        }

        Setter setter = new Setter( field, applicationContext, entityHandler );

        storage.put( id, setter );

        return toSetter( field );
    }


    public static class Setter {

        private final Logger logger = LoggerFactory.getLogger( this.getClass() );

        private final ApplicationContext applicationContext;
        private final EntityHandler      entityHandler;

        private String requestParameter;

        private Method method;

        private Class< ? > methodParameter;

        private boolean isArrayOrCollection;

        private boolean isRelation;

        private EntityHandler.Entity relationType;

        private Class< ? > genericType;

        private final Field field;

        private boolean isComputed;

        private final List< CustomConstraint > customConstraints;


        public Setter( Field field, ApplicationContext applicationContext, EntityHandler entityHandler )
                throws InvalidSetterParameterType,
                       ToManySetterParameterException,
                       MultipleSetterFoundException,
                       SetterNotFoundException,
                       NoSuchMethodException {
            this.field              = field;
            this.applicationContext = applicationContext;
            this.entityHandler      = entityHandler;
            customConstraints       = new ArrayList<>();
            compute();
        }


        public void invokeWithValue( Object entity, Object newValue )
                throws InvocationTargetException, IllegalAccessException {
            if ( isArrayOrCollection && !isRelation ) {
                if ( newValue == null ) {
                    callConstraint( entity, null );
                    method.invoke( entity, TypeResolver.castTo( genericType, null ) );
                    return;
                }

                List< Object > values = ( List< Object > ) newValue;

                for ( Object value : values ) {
                    callConstraint( entity, value );
                    method.invoke( entity, TypeResolver.castTo( genericType, value ) );
                }

                return;
            } else if ( isRelation && !isArrayOrCollection ) {
                Object relation;

                if ( newValue != null && TypeResolver.isWrapperOrPrimitive( newValue ) ) {

                    Long              value             = ( Long ) TypeResolver.castTo( Long.class, newValue );
                    DefaultRepository defaultRepository = entityHandler.getEntity( relationType.getSubject() ).getDefaultRepository();
                    relation = defaultRepository.findOrFail( value );
                } else {
                    relation = newValue;
                }

                callConstraint( entity, relation );
                method.invoke( entity, relation );
                return;
            } else if ( isArrayOrCollection ) {
                if ( newValue == null ) {
                    callConstraint( entity, null );
                    method.invoke( entity, TypeResolver.castTo( genericType, null ) );
                    return;
                }

                if ( !newValue.getClass().isArray() && !newValue.getClass().isAssignableFrom( Collection.class ) ) {
                    Object relation;

                    if ( TypeResolver.isWrapperOrPrimitive( newValue ) ) {
                        Long value = ( Long ) TypeResolver.castTo( Long.class, newValue );

                        DefaultRepository defaultRepository = entityHandler.getEntity( relationType.getSubject() ).getDefaultRepository();
                        relation = defaultRepository.findOrFail( value );

                        callConstraint( entity, relation );
                        method.invoke( entity, relation );
                    } else {
                        relation = newValue;
                    }

                    callConstraint( entity, relation );
                    method.invoke( entity, relation );

                    return;
                }

                List< Object > values = ( List< Object > ) newValue;

                DefaultRepository defaultRepository = entityHandler.getEntity( relationType.getSubject() ).getDefaultRepository();

                for ( Object value : values ) {
                    Long castedValue = ( Long ) TypeResolver.castTo( Long.class, value );

                    callConstraint( entity, castedValue );

                    method.invoke( entity, defaultRepository.findOrFail( castedValue ) );
                }

                return;
            }

            Object value = TypeResolver.castTo( methodParameter, newValue );

            callConstraint( entity, value );

            method.invoke( entity, value );
        }


        public void invoke( Request request, Object entity )
                throws InvocationTargetException, IllegalAccessException {

            logger.debug( "Search in request parameter \"" + requestParameter + "\"" );


            if ( isArrayOrCollection ) {
                List< Object > values = request.getParameters( requestParameter );

                invokeWithValue( entity, values );

                return;
            }

            invokeWithValue( entity, request.getParameter( requestParameter ) );
        }


        public Method getMethod() {
            return method;
        }


        public Field getField() {
            return field;
        }


        public String getRequestParameter() {
            return requestParameter;
        }


        private void callConstraint( Object entity, Object newValue ) {
            for ( CustomConstraint customConstraint : customConstraints ) {
                customConstraint.check( entity, newValue );
            }
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
                    relationType = entityHandler.getEntity( field.getType() );
                } else {
                    ParameterizedType parameterizedType = ( ParameterizedType ) field.getGenericType();
                    relationType = entityHandler.getEntity( ( Class< ? > ) parameterizedType.getActualTypeArguments()[ 0 ] );
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

            if ( methodParameter == null ) {
                logger.error( "Unable to resolve parameter type for setter " + method.getName() + " in " + method.getDeclaringClass().getName() );
            }

            if ( !isArrayOrCollection
                    && !isRelation
                    && (TypeResolver.toWrapper( ( Class< ? > ) type ) == null || !methodParameter.getName().equals( TypeResolver.toWrapper( ( Class< ? > ) type ).getName() )) ) {
                throw new InvalidSetterParameterType( method );
            }

            RequestParameter requestParameter = field.getAnnotation( RequestParameter.class );

            if ( requestParameter != null ) {
                this.requestParameter = requestParameter.name();
            } else {
                if ( !isRelation ) {
                    this.requestParameter = com.replace.replace.api.poc.kernel.util.Formatter.toSnakeCase( field.getDeclaringClass().getSimpleName() + "_" + field.getName() );
                } else {
                    this.requestParameter = com.replace.replace.api.poc.kernel.util.Formatter.toSnakeCase( field.getDeclaringClass().getSimpleName() + "_" + field.getName() + "_id" );
                }
            }

            Constraint constraint = field.getAnnotation( Constraint.class );

            if ( constraint != null ) {
                for ( Class< ? extends CustomConstraint > customConstraintClass : constraint.value() ) {
                    customConstraints.add( applicationContext.getBean( customConstraintClass ) );
                }
            }

            isComputed = true;
        }


        protected Method searchSetter()
                throws SetterNotFoundException, MultipleSetterFoundException, NoSuchMethodException {
            com.replace.replace.api.poc.annotation.Setter setter = field.getAnnotation( com.replace.replace.api.poc.annotation.Setter.class );


            List< String > searchs = new ArrayList<>();
            List< Method > founds  = new ArrayList<>();

            if ( isArrayOrCollection ) {
                searchs.add( "add" + com.replace.replace.api.poc.kernel.util.Formatter.toPascalCase( field.getName() ).replaceFirst( "s$", "" ) );
                searchs.add( "add" + com.replace.replace.api.poc.kernel.util.Formatter.toPascalCase( field.getName() ) );
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
