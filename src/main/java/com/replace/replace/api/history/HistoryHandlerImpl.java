package com.replace.replace.api.history;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Service
@RequestScope
public class HistoryHandlerImpl implements HistoryHandler {
    private final Map< Object, History > store;
    private       EntityManager          entityManager;


    public HistoryHandlerImpl(
            EntityManager entityManager ) {
        this.entityManager = entityManager;
        this.store         = new HashMap<>();
    }


    @Override
    public void create( Object object ) {
        assert object != null : "variable object should not be null";

        History history = new History();
        history.setAuthorId( 0 );
        history.setAuthorType( "Unknow" );
        history.setSubjectType( object.getClass().getName() );
        history.setLogType( History.TYPE_CREATE );
        history.setIpAddress( this.getRemoteAddr() );

        this.store.put( object, history );
    }


    @Override
    public void update( Object object, String property ) {

        assert object != null : "variable object should not be null";

        History history = new History();
        history.setAuthorId( 0 );
        history.setAuthorType( "Unknow" );
        history.setSubjectType( object.getClass().getName() );
        history.setSubjectProperty( property );
        history.setNewValue( this.getFieldValue( object, property ) );
        history.setLogType( History.TYPE_UPDATE );
        history.setIpAddress( this.getRemoteAddr() );

        this.store.put( object, history );
    }


    @Override
    public void delete( Object object ) {
        assert object != null : "variable object should not be null";
        
        History history = new History();
        history.setAuthorId( 0 );
        history.setAuthorType( "Unknow" );
        history.setSubjectId( Integer.valueOf( this.getFieldValue( object, "id" ) ) );
        history.setSubjectType( object.getClass().getName() );
        history.setLogType( History.TYPE_DELETE );
        history.setIpAddress( this.getRemoteAddr() );

        this.entityManager.persist( history );
    }


    /**
     * Return new value of subject property target
     *
     * @param object
     * @param property
     * @return
     */
    private String getFieldValue( Object object, String property ) {
        Field reflectionProperty = null;

        try {
            reflectionProperty = object.getClass().getDeclaredField( property );
            reflectionProperty.setAccessible( true );
            return String.valueOf( reflectionProperty.get( object ) );
        } catch ( NoSuchFieldException | IllegalAccessException e ) {
            e.printStackTrace();
        }

        return null;
    }


    private String getRemoteAddr() {
        return (( ServletRequestAttributes ) RequestContextHolder.currentRequestAttributes())
                .getRequest()
                .getRemoteAddr();
    }


    @Override
    public void receiveEvent( String event, Map< String, Object > params ) throws RuntimeException {
        for ( Map.Entry< Object, History > entry : this.store.entrySet() ) {
            History history = entry.getValue();

            history.setSubjectId( Integer.parseInt( this.getFieldValue( entry.getKey(), "id" ) ) );


            this.entityManager.persist( history );
        }
    }
}
