package com.fairfair.callcenter.api.history;

import com.fairfair.callcenter.api.request.Request;
import com.fairfair.callcenter.configuration.event.Event;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequestScope
public class HistoryHandlerImpl implements HistoryHandler {
    private final Map< Object, History >    store;
    private final EntityManager             entityManager;
    private final Request                   request;
    private final List< HistorySubscriber > historySubscribers;
    private final HistoryConfigurer         historyConfigurer;


    public HistoryHandlerImpl(
            final EntityManager entityManager,
            Request request,
            List< HistorySubscriber > historySubscribers,
            HistoryConfigurer historyConfigurer ) {
        this.entityManager      = entityManager;
        this.request            = request;
        this.historyConfigurer  = historyConfigurer;
        this.store              = new HashMap<>();
        this.historySubscribers = historySubscribers;
    }


    @Override
    public void create( final Object object ) {
        assert object != null : "variable object should not be null";

        final History history = new History();
        history.setAuthorId( historyConfigurer.getAuthorId().orElse( 0 ) );
        history.setAuthorType( historyConfigurer.getAuthorName().orElse( "Unknow" ) );
        history.setSubjectType( object.getClass().getName() );
        history.setLogType( History.TYPE_CREATE );
        history.setIpAddress( historyConfigurer.getAuthorIp().orElse( null ) );
        history.setUri( request.getUri() );

        this.store.put( object, history );

        for ( HistorySubscriber historySubscriber : historySubscribers ) {
            historySubscriber.create( object, history );
        }
    }


    @Override
    public void update( final Object object, final String property ) {

        assert object != null : "variable object should not be null";

        final History history = new History();
        history.setAuthorId( historyConfigurer.getAuthorId().orElse( 0 ) );
        history.setAuthorType( historyConfigurer.getAuthorName().orElse( "Unknow" ) );
        history.setSubjectType( object.getClass().getName() );
        history.setSubjectProperty( property );
        history.setNewValue( this.getFieldValue( object, property ) );
        history.setLogType( History.TYPE_UPDATE );
        history.setIpAddress( historyConfigurer.getAuthorIp().orElse( null ) );
        history.setUri( request.getUri() );

        this.store.put( object, history );

        for ( HistorySubscriber historySubscriber : historySubscribers ) {
            historySubscriber.update( object, property, history );
        }
    }


    @Override
    public void delete( final Object object ) {
        assert object != null : "variable object should not be null";

        final History history = new History();
        history.setAuthorId( historyConfigurer.getAuthorId().orElse( 0 ) );
        history.setAuthorType( historyConfigurer.getAuthorName().orElse( "Unknow" ) );
        history.setSubjectId( Integer.valueOf( this.getFieldValue( object, "id" ) ) );
        history.setSubjectType( object.getClass().getName() );
        history.setLogType( History.TYPE_DELETE );
        history.setIpAddress( historyConfigurer.getAuthorIp().orElse( null ) );
        history.setUri( request.getUri() );

        this.entityManager.persist( history );

        for ( HistorySubscriber historySubscriber : historySubscribers ) {
            historySubscriber.delete( object, history );
        }
    }


    /**
     * Return new value of subject property target
     *
     * @param object
     * @param property
     * @return
     */
    private String getFieldValue( final Object object, final String property ) {
        Field reflectionProperty = null;

        Class objectClass = object.getClass();

        while ( true ) {
            try {
                reflectionProperty = objectClass.getDeclaredField( property );
                reflectionProperty.setAccessible( true );
                return String.valueOf( reflectionProperty.get( object ) );
            } catch ( final NoSuchFieldException | IllegalAccessException e ) {
                if ( objectClass.getSuperclass() != null ) {
                    objectClass = objectClass.getSuperclass();
                    continue;
                }

                return null;
            }
        }
    }


    @Override
    public List< Event > getEvents() {
        return List.of(
                Event.TRANSACTION_SUCCESS
        );
    }


    @Override
    public void receiveEvent( final Event event, final Map< String, Object > params ) throws RuntimeException {
        for ( final Map.Entry< Object, History > entry : this.store.entrySet() ) {
            final History history = entry.getValue();

            history.setSubjectId( Integer.parseInt( this.getFieldValue( entry.getKey(), "id" ) ) );


            this.entityManager.persist( history );
        }
    }


    @Override
    public int getPriority() {
        return 0;
    }
}
