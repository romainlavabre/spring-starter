package com.replace.replace.api.poc.kernel.entry;

import com.replace.replace.api.json.Encoder;
import com.replace.replace.api.poc.annotation.*;
import com.replace.replace.api.poc.kernel.entity.EntityHandler;
import com.replace.replace.api.poc.kernel.exception.NoRouteMatchException;
import com.replace.replace.api.poc.kernel.router.RouteHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.configuration.json.GroupType;
import com.replace.replace.repository.DefaultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Controller {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    protected final CreateEntry        createEntry;
    protected final UpdateEntry        updateEntry;
    protected final DeleteEntry        deleteEntry;
    protected final RouteHandler       routeHandler;
    protected final EntityHandler      entityHandler;
    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;
    protected final ApplicationContext applicationContext;


    public Controller(
            final CreateEntry createEntry,
            final UpdateEntry updateEntry,
            final DeleteEntry deleteEntry,
            final RouteHandler routeHandler,
            final EntityHandler entityHandler,
            final DataStorageHandler dataStorageHandler,
            final Request request,
            final ApplicationContext applicationContext ) {
        this.createEntry        = createEntry;
        this.updateEntry        = updateEntry;
        this.deleteEntry        = deleteEntry;
        this.routeHandler       = routeHandler;
        this.entityHandler      = entityHandler;
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
        this.applicationContext = applicationContext;
    }


    public ResponseEntity< Map< String, Object > > getOne( @PathVariable( "id" ) final long id )
            throws NoRouteMatchException,
                   IllegalAccessException {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, GetOne.class );

        final DefaultRepository< ? > defaultRepository = this.entityHandler.getEntity( route.getSubject() ).getDefaultRepository();

        return ResponseEntity.ok(
                Encoder.encode( defaultRepository.findOrFail( id ), this.getGroup( route.getRole() ) )
        );
    }


    public ResponseEntity< List< Map< String, Object > > > getAll()
            throws NoRouteMatchException,
                   IllegalAccessException {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, GetAll.class );

        final DefaultRepository< ? > defaultRepository = this.applicationContext.getBean( route.getRepository() );

        return ResponseEntity.ok(
                Encoder.encode( defaultRepository.findAll(), this.getGroup( route.getRole() ) )
        );
    }


    public ResponseEntity< Map< String, Object > > getOneBy( @PathVariable( "id" ) final long id )
            throws Throwable {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, GetOneBy.class );

        final DefaultRepository< ? > relationRepository = this.entityHandler.getEntity( (( GetOneBy ) route.getHttpType()).entity() ).getDefaultRepository();

        final Object relation = relationRepository.findOrFail( id );

        final DefaultRepository< ? > defaultRepository = this.applicationContext.getBean( route.getRepository() );

        final Method method = defaultRepository.getClass().getDeclaredMethod( route.getRepositoryMethod(), (( GetOneBy ) route.getHttpType()).entity() );

        try {
            return ResponseEntity.ok(
                    Encoder.encode( method.invoke( defaultRepository, relation ), this.getGroup( route.getRole() ) )
            );
        } catch ( final Throwable throwable ) {
            throw throwable.getCause();
        }
    }


    public ResponseEntity< List< Map< String, Object > > > getAllBy( @PathVariable( "id" ) final long id )
            throws Throwable {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, GetAllBy.class );

        final DefaultRepository< ? > relationRepository = this.applicationContext.getBean( this.entityHandler.getEntity( (( GetAllBy ) route.getHttpType()).entity() ).getRepository() );

        final Object relation = relationRepository.findOrFail( id );

        final DefaultRepository< ? > defaultRepository = this.applicationContext.getBean( route.getRepository() );

        final Method method = defaultRepository.getClass().getDeclaredMethod( route.getRepositoryMethod(), (( GetAllBy ) route.getHttpType()).entity() );

        try {
            return ResponseEntity.ok(
                    Encoder.encode( ( List< ? extends Object > ) method.invoke( defaultRepository, relation ), this.getGroup( route.getRole() ) )
            );
        } catch ( final Throwable throwable ) {
            throw throwable.getCause();
        }
    }


    @Transactional
    public ResponseEntity< Map< String, Object > > post()
            throws Throwable {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, Post.class );

        final Object subject = route.getSubject().getDeclaredConstructor().newInstance();

        this.createEntry.create( this.request, subject, route );

        this.dataStorageHandler.save();

        return ResponseEntity
                .status( HttpStatus.CREATED )
                .body( Encoder.encode( subject, this.getGroup( route.getRole() ) ) );
    }


    @Transactional
    public ResponseEntity< Map< String, Object > > put( @PathVariable( "id" ) final long id )
            throws Throwable {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, Put.class );

        final DefaultRepository< ? > defaultRepository = this.entityHandler.getEntity( route.getSubject() ).getDefaultRepository();
        final Object                 subject           = defaultRepository.findOrFail( id );

        this.updateEntry.update( this.request, subject, route );

        this.dataStorageHandler.save();

        return ResponseEntity.ok(
                Encoder.encode( subject, this.getGroup( route.getRole() ) )
        );
    }


    @Transactional
    public ResponseEntity< Void > patch( @PathVariable( "id" ) final long id )
            throws Throwable {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, Patch.class );

        final DefaultRepository< ? > defaultRepository = this.entityHandler.getEntity( route.getSubject() ).getDefaultRepository();
        final Object                 subject           = defaultRepository.findOrFail( id );

        this.updateEntry.update( this.request, subject, route );

        this.dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    @Transactional
    public ResponseEntity< Void > delete( @PathVariable( "id" ) final long id )
            throws Throwable {
        final RouteHandler.Route route = this.routeHandler.getRoute( this.request, GetOne.class );

        final DefaultRepository< ? > defaultRepository = this.applicationContext.getBean( route.getRepository() );

        final Object subject = defaultRepository.findOrFail( id );

        this.deleteEntry.delete( this.request, subject, route );

        this.dataStorageHandler.save();

        return ResponseEntity.noContent().build();
    }


    private String getGroup( final String role ) throws IllegalAccessException {
        final Field field;

        if ( role == null ) {
            return "GUEST";
        }

        try {
            field = GroupType.class.getDeclaredField( role.replaceFirst( "ROLE_", "" ).toUpperCase() );
        } catch ( final NoSuchFieldException e ) {
            this.logger.error( "No json group found for " + role );
            return GroupType.DEFAULT;
        }

        return String.valueOf( field.get( null ) );
    }
}
