package com.fairfair.callcenter.system.event;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.event.EventSubscriber;
import com.fairfair.callcenter.api.history.History;
import com.fairfair.callcenter.api.history.HistorySubscriber;
import com.fairfair.callcenter.api.json.Encoder;
import com.fairfair.callcenter.configuration.environment.Variable;
import com.fairfair.callcenter.configuration.event.Event;
import com.fairfair.callcenter.configuration.json.GroupType;
import com.fairfair.callcenter.configuration.system.event.BlackList;
import com.fairfair.callcenter.system.action.Action;
import com.fairfair.callcenter.system.action.ActionHandler;
import com.fairfair.callcenter.system.action.RequestMethodType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class DataEventHandlerImpl implements DataEventHandler, EventSubscriber, HistorySubscriber {

    private static final String         CREATED = "CREATED";
    private static final String         UPDATED = "UPDATED";
    private static final String         DELETED = "DELETED";
    protected final      ActionHandler  actionHandler;
    protected final      Environment    environment;
    protected final      List< Object > created;
    protected final      List< Object > updated;
    protected final      List< Object > deleted;


    public DataEventHandlerImpl(
            ActionHandler actionHandler,
            Environment environment ) {
        this.actionHandler = actionHandler;
        this.environment   = environment;
        created            = new ArrayList<>();
        updated            = new ArrayList<>();
        deleted            = new ArrayList<>();
    }


    @Override
    public void created( Object entity ) {
        if ( created.contains( entity ) ) {
            return;
        }

        created.add( entity );
    }


    @Override
    public void updated( Object entity ) {
        if ( updated.contains( entity ) ) {
            return;
        }

        updated.add( entity );
    }


    @Override
    public void deleted( Object entity ) {
        if ( deleted.contains( entity ) ) {
            return;
        }

        deleted.add( entity );
    }


    @Override
    public void create( Object object, History history ) {
        created( object );
    }


    @Override
    public void update( Object object, String property, History history ) {
        updated( object );
    }


    @Override
    public void delete( Object object, History history ) {
        deleted( object );
    }


    protected void collectEvent() {
        for ( Object entity : created ) {
            buildAction( entity, CREATED );
        }

        for ( Object entity : updated ) {
            buildAction( entity, UPDATED );
        }

        for ( Object entity : deleted ) {
            buildAction( entity, DELETED );
        }
    }


    protected void buildAction( Object entity, String actionType ) {
        if ( BlackList.get().contains( entity.getClass() ) ) {
            return;
        }

        Action action = new Action();

        Map< String, Object > entityEncoded = Encoder.encode( entity, GroupType.DATA_REPOSITORY );

        for ( Map.Entry< String, Object > entry : entityEncoded.entrySet() ) {
            if ( entry.getValue() == null ) {
                entityEncoded.put( entry.getKey(), "JSON.NULL" );
            }
        }

        action.setUrl( getEvent( entity ) )
                .setMethod( RequestMethodType.POST )
                .setTimeOut( 60 * 10L )
                .addRequestPayload( "metadata", Map.of(
                        "entity", entity.getClass().getSimpleName().split( "\\$" )[ 0 ],
                        "version", Instant.now().toEpochMilli(),
                        "action", actionType,
                        "origin", environment.getEnv( Variable.APPLICATION_NAME )
                ) )
                .addRequestPayload( "entity", entityEncoded );

        actionHandler.save( action );
    }


    protected String getEvent( Object entity ) {
        String event = environment.getEnv( Variable.APPLICATION_NAME ).replace( "-", "_" ).toUpperCase();

        char[] letters = entity.getClass().getSimpleName().toCharArray();

        StringBuilder entityName = new StringBuilder();

        for ( int i = 0; i < letters.length; i++ ) {
            String letter = String.valueOf( letters[ i ] );

            if ( i > 0 && letter.matches( "[A-Z]" ) ) {
                entityName.append( "_" )
                        .append( letter.toUpperCase() );
                continue;
            }

            entityName.append( letter.toUpperCase() );
        }

        return "EVENT::" + event + "_" + entityName.toString().split( "\\$" )[ 0 ] + "_UPDATED";
    }


    @Override
    public List< Event > getEvents() {
        return List.of(
                Event.TRANSACTION_SUCCESS
        );
    }


    @Override
    public void receiveEvent( Event event, Map< String, Object > params ) throws RuntimeException {
        collectEvent();
    }


    @Override
    public int getPriority() {
        return 0;
    }
}
