package com.replace.replace.api.dynamic.kernel.entry;

import com.replace.replace.api.dynamic.annotation.GetAll;
import com.replace.replace.api.dynamic.annotation.GetOne;
import com.replace.replace.api.dynamic.annotation.GetOneBy;
import com.replace.replace.api.dynamic.kernel.entity.EntityHandler;
import com.replace.replace.api.dynamic.kernel.exception.NoRouteMatchException;
import com.replace.replace.api.dynamic.kernel.router.RouteHandler;
import com.replace.replace.api.json.Encoder;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Controller {

    protected final Logger             logger = LoggerFactory.getLogger( this.getClass() );
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;
    protected final ApplicationContext applicationContext;


    public Controller(
            DataStorageHandler dataStorageHandler,
            Request request,
            ApplicationContext applicationContext ) {
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
        this.applicationContext = applicationContext;
    }


    public ResponseEntity< Map< String, Object > > getOne( @PathVariable( "id" ) long id )
            throws NoRouteMatchException,
                   IllegalAccessException {
        RouteHandler.Route route = RouteHandler.getRoute( request, GetOne.class );

        DefaultRepository< ? > defaultRepository = applicationContext.getBean( route.getRepository() );

        return ResponseEntity.ok(
                Encoder.encode( defaultRepository.findOrFail( id ), getGroup( route.getRole() ) )
        );
    }


    public ResponseEntity< List< Map< String, Object > > > getAll()
            throws NoRouteMatchException,
                   IllegalAccessException {
        RouteHandler.Route route = RouteHandler.getRoute( request, GetAll.class );

        DefaultRepository< ? > defaultRepository = applicationContext.getBean( route.getRepository() );

        return ResponseEntity.ok(
                Encoder.encode( defaultRepository.findAll(), getGroup( route.getRole() ) )
        );
    }


    public ResponseEntity< Map< String, Object > > getOneBy( @PathVariable( "id" ) long id )
            throws NoRouteMatchException,
                   IllegalAccessException,
                   NoSuchMethodException,
                   InvocationTargetException {
        RouteHandler.Route route = RouteHandler.getRoute( request, GetOneBy.class );

        DefaultRepository< ? > relationRepository = applicationContext.getBean( EntityHandler.getEntity( (( GetOneBy ) route.getHttpType()).entity() ).getRepository() );

        Object relation = relationRepository.findOrFail( id );

        DefaultRepository< ? > defaultRepository = applicationContext.getBean( route.getRepository() );

        Method method = defaultRepository.getClass().getDeclaredMethod( route.getRepositoryMethod(), (( GetOneBy ) route.getHttpType()).entity() );

        return ResponseEntity.ok(
                Encoder.encode( method.invoke( defaultRepository, relation ), getGroup( route.getRole() ) )
        );
    }


    public ResponseEntity< List< Map< String, Object > > > getAllBy( @PathVariable( "id" ) long id ) {
        return null;
    }


    @Transactional
    public ResponseEntity< Map< String, Object > > post() {
        return null;
    }


    @Transactional
    public ResponseEntity< Map< String, Object > > put( @PathVariable( "id" ) long id ) {
        return null;
    }


    @Transactional
    public ResponseEntity< Void > patch( @PathVariable( "id" ) long id ) {
        return null;
    }


    @Transactional
    public ResponseEntity< Void > delete( @PathVariable( "id" ) long id ) {
        return null;
    }


    private String getGroup( String role ) throws IllegalAccessException {
        Field field;
        try {
            field = GroupType.class.getDeclaredField( role.replaceFirst( "ROLE_", "" ).toUpperCase() );
        } catch ( NoSuchFieldException e ) {
            logger.error( "No json group found for " + role );
            return GroupType.DEFAULT;
        }

        return String.valueOf( field.get( null ) );
    }
}
