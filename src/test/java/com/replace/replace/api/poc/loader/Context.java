package com.replace.replace.api.poc.loader;

import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.poc.kernel.entity.EntityHandler;
import com.replace.replace.api.poc.kernel.entry.*;
import com.replace.replace.api.poc.kernel.router.Resolver;
import com.replace.replace.api.poc.kernel.router.RouteHandler;
import com.replace.replace.api.poc.kernel.setter.SetterHandler;
import com.replace.replace.api.poc.kernel.trigger.TriggerHandler;
import com.replace.replace.api.poc.loader.mock.MockApplicationContext;
import com.replace.replace.api.poc.loader.mock.MockRequest;
import com.replace.replace.api.poc.loader.mock.MockRequestMappingHandlerMapping;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public final class Context {

    private static Context                   context;
    private final  Map< Class< ? >, Object > mocks = new HashMap<>();


    private Context() {
        context = this;
        loadContext();
    }


    public static < T > T getMock( Class< T > type ) {
        init();
        return ( T ) (( MockApplicationContext ) context.mocks.get( ApplicationContext.class )).getMock( type );
    }


    public static List< Object > getAllMocks() {
        init();
        return (( MockApplicationContext ) context.mocks.get( ApplicationContext.class )).getAllMock();
    }


    public static MockRequestMappingHandlerMapping getRouteRepository() {
        init();
        return ( MockRequestMappingHandlerMapping ) context.mocks.get( RequestMappingHandlerMapping.class );
    }


    public static MockRequest getMockRequest() {
        init();
        return ( MockRequest ) context.mocks.get( Request.class );
    }


    private void loadContext() {
        Resolver resolver = new Resolver(
                getRequestMappingHandlerMapping(),
                getApplicationContext(),
                getRouteHandler(),
                getTriggerHandler(),
                getEntityHandler(),
                getController()
        );

        new Trigger(
                ( Create ) getCreateEntry(),
                ( Update ) getUpdateEntry(),
                ( Delete ) getDeleteEntry(),
                getSetterHandler(),
                getTriggerHandler(),
                getApplicationContext()
        );

        Assertions.assertDoesNotThrow( resolver::resolveRouter );
    }


    private Controller getController() {
        return new Controller(
                context.getCreateEntry(),
                context.getUpdateEntry(),
                context.getDeleteEntry(),
                context.getRouteHandler(),
                context.getEntityHandler(),
                context.getDataStorageHandler(),
                context.getRequest(),
                context.getApplicationContext()
        );
    }


    private Request getRequest() {
        if ( mocks.containsKey( Request.class ) ) {
            return ( Request ) mocks.get( Request.class );
        }

        mocks.put( Request.class, new MockRequest() );

        return ( Request ) mocks.get( Request.class );
    }


    private CreateEntry getCreateEntry() {
        if ( mocks.containsKey( CreateEntry.class ) ) {
            return ( CreateEntry ) mocks.get( CreateEntry.class );
        }

        mocks.put( CreateEntry.class, new Create(
                getHistoryHandler(),
                getTriggerHandler(),
                getSetterHandler(),
                getEntityHandler()
        ) );

        return ( CreateEntry ) mocks.get( CreateEntry.class );
    }


    private UpdateEntry getUpdateEntry() {
        if ( mocks.containsKey( UpdateEntry.class ) ) {
            return ( UpdateEntry ) mocks.get( UpdateEntry.class );
        }

        mocks.put( UpdateEntry.class, new Update(
                getHistoryHandler(),
                getTriggerHandler(),
                getEntityHandler()
        ) );

        return ( UpdateEntry ) mocks.get( UpdateEntry.class );
    }


    private DeleteEntry getDeleteEntry() {
        if ( mocks.containsKey( DeleteEntry.class ) ) {
            return ( DeleteEntry ) mocks.get( DeleteEntry.class );
        }

        mocks.put( DeleteEntry.class, new Delete(
                getHistoryHandler(),
                getEntityHandler()
        ) );

        return ( DeleteEntry ) mocks.get( DeleteEntry.class );
    }


    private HistoryHandler getHistoryHandler() {
        if ( mocks.containsKey( HistoryHandler.class ) ) {
            return ( HistoryHandler ) mocks.get( HistoryHandler.class );
        }

        mocks.put( HistoryHandler.class, Mockito.mock( HistoryHandler.class ) );

        return ( HistoryHandler ) mocks.get( HistoryHandler.class );
    }


    private TriggerHandler getTriggerHandler() {
        if ( mocks.containsKey( TriggerHandler.class ) ) {
            return ( TriggerHandler ) mocks.get( TriggerHandler.class );
        }

        mocks.put( TriggerHandler.class, new TriggerHandler(
                getApplicationContext(),
                getSetterHandler(),
                getEntityHandler()
        ) );

        return ( TriggerHandler ) mocks.get( TriggerHandler.class );
    }


    private SetterHandler getSetterHandler() {
        if ( mocks.containsKey( SetterHandler.class ) ) {
            return ( SetterHandler ) mocks.get( SetterHandler.class );
        }

        mocks.put( SetterHandler.class, new SetterHandler(
                getApplicationContext(),
                getEntityHandler()
        ) );

        return ( SetterHandler ) mocks.get( SetterHandler.class );
    }


    private RouteHandler getRouteHandler() {
        if ( mocks.containsKey( RouteHandler.class ) ) {
            return ( RouteHandler ) mocks.get( RouteHandler.class );
        }

        mocks.put( RouteHandler.class, new RouteHandler(
                getApplicationContext(),
                getSetterHandler(),
                getEntityHandler()
        ) );

        return ( RouteHandler ) mocks.get( RouteHandler.class );
    }


    private EntityHandler getEntityHandler() {
        if ( mocks.containsKey( EntityHandler.class ) ) {
            return ( EntityHandler ) mocks.get( EntityHandler.class );
        }

        mocks.put( EntityHandler.class, new com.replace.replace.api.poc.loader.mock.EntityHandler() );

        return ( EntityHandler ) mocks.get( EntityHandler.class );
    }


    private ApplicationContext getApplicationContext() {
        if ( mocks.containsKey( ApplicationContext.class ) ) {
            return ( ApplicationContext ) mocks.get( ApplicationContext.class );
        }

        mocks.put( ApplicationContext.class, new MockApplicationContext() );

        return ( ApplicationContext ) mocks.get( ApplicationContext.class );
    }


    private RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        if ( mocks.containsKey( RequestMappingHandlerMapping.class ) ) {
            return ( RequestMappingHandlerMapping ) mocks.get( RequestMappingHandlerMapping.class );
        }

        mocks.put( RequestMappingHandlerMapping.class, new MockRequestMappingHandlerMapping() );

        return ( RequestMappingHandlerMapping ) mocks.get( RequestMappingHandlerMapping.class );
    }


    private DataStorageHandler getDataStorageHandler() {
        return Mockito.mock( DataStorageHandler.class );
    }


    private static void init() {
        if ( context == null ) {
            new Context();
        }
    }
}
