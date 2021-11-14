package com.replace.replace.api.poc.client;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.crud.Delete;
import com.replace.replace.api.crud.Update;
import com.replace.replace.api.poc.api.CustomConstraint;
import com.replace.replace.api.poc.api.ResourceProvider;
import com.replace.replace.api.poc.api.UnmanagedTrigger;
import com.replace.replace.api.poc.loader.Context;
import com.replace.replace.api.request.Request;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class PocMock {

    protected final PocClient pocClient;


    public PocMock() {
        this.pocClient = new PocClient();
        init();
    }


    public < T > T getMock( Class< T > type ) {
        T object = Context.getMock( type );

        if ( object == null ) {
            throw new NullPointerException( "Mock not found" );
        }

        Mockito.reset( object );

        return object;
    }


    private void init() {
        for ( Object object : Context.getAllMocks() ) {
            if ( object instanceof ResourceProvider ) {
                Mockito.when( (( ResourceProvider ) object).getResources( Mockito.any() ) ).thenCallRealMethod();
                injectDependencies( object );
            }

            if ( object instanceof CustomConstraint ) {
                Mockito.doCallRealMethod().when( (( CustomConstraint ) object) ).check( Mockito.any(), Mockito.any() );
                injectDependencies( object );
            }

            if ( object instanceof UnmanagedTrigger ) {
                Mockito.doCallRealMethod().when( (( UnmanagedTrigger ) object) ).handle( Mockito.any(), Mockito.any() );
                injectDependencies( object );
            }

            if ( object instanceof Create ) {
                Mockito.doCallRealMethod().when( (( Create ) object) ).create( Mockito.any( Request.class ), Mockito.any() );
                injectDependencies( object );
            }

            if ( object instanceof Update ) {
                Mockito.doCallRealMethod().when( (( Update ) object) ).update( Mockito.any( Request.class ), Mockito.any() );
                injectDependencies( object );
            }

            if ( object instanceof Delete ) {
                Mockito.doCallRealMethod().when( (( Delete ) object) ).delete( Mockito.any( Request.class ), Mockito.any() );
                injectDependencies( object );
            }
        }
    }


    private void injectDependencies( Object object ) {
        firstLoop:
        for ( Field field : object.getClass().getSuperclass().getDeclaredFields() ) {

            Constructor[] constructors = object.getClass().getSuperclass().getDeclaredConstructors();

            if ( constructors.length > 0 ) {
                Constructor constructor = constructors[ 0 ];

                Class< ? >[] parameterTypes = constructor.getParameterTypes();

                for ( int i = 0; i < parameterTypes.length; i++ ) {
                    if ( parameterTypes[ i ].equals( field.getType() ) ) {
                        field.setAccessible( true );
                        try {
                            field.set( object, Context.getMock( field.getType() ) );
                        } catch ( IllegalAccessException e ) {
                            e.printStackTrace();
                        }

                        continue firstLoop;
                    }
                }
            }
        }
    }


    private Object[] getArgs( Method method ) {
        List< Object > args = new ArrayList<>();

        for ( int i = 0; i < method.getParameterCount(); i++ ) {
            args.add( Mockito.any( Object.class ) );
        }

        return args.toArray();
    }
}
