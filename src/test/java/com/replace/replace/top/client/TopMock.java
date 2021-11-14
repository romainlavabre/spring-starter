package com.replace.replace.top.client;

import com.replace.replace.api.top.api.CustomConstraint;
import com.replace.replace.api.top.api.ResourceProvider;
import com.replace.replace.api.top.api.UnmanagedTrigger;
import com.replace.replace.top.loader.Context;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TopMock {

    protected final TopClient topClient;


    public TopMock() {
        this.topClient = new TopClient();
        init();
    }


    public < T > T getMock( Class< T > type ) {
        T object = Context.getMock( type );

        Mockito.reset( object );

        return object;
    }


    private void init() {
        for ( Object object : Context.getAllMocks() ) {
            if ( object instanceof ResourceProvider ) {
                Mockito.when( (( ResourceProvider ) object).getResources( Mockito.any() ) ).thenCallRealMethod();
            }

            if ( object instanceof CustomConstraint ) {
                Mockito.doCallRealMethod().when( (( CustomConstraint ) object) ).check( Mockito.any(), Mockito.any() );
            }

            if ( object instanceof UnmanagedTrigger ) {
                Mockito.doCallRealMethod().when( (( UnmanagedTrigger ) object) ).handle( Mockito.any(), Mockito.any() );
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
