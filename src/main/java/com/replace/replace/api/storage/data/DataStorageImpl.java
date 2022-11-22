package com.replace.replace.api.storage.data;

import com.replace.replace.api.event.EventDispatcher;
import com.replace.replace.configuration.event.Event;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DataStorageImpl implements DataStorageHandler {

    protected static final Map< Integer, LockModeType > LOCK_TYPE = Map.of(
            4, LockModeType.PESSIMISTIC_READ,
            5, LockModeType.PESSIMISTIC_WRITE
    );
    protected final        EntityManager                entityManager;
    protected final        EventDispatcher              eventDispatcher;
    protected final        StampManagedEntity           stampManagedEntity;
    protected final        ApplicationEventPublisher    applicationEventPublisher;


    public DataStorageImpl(
            final EntityManager entityManager,
            final EventDispatcher eventDispatcher,
            StampManagedEntity stampManagedEntity,
            ApplicationEventPublisher applicationEventPublisher ) {
        this.entityManager             = entityManager;
        this.eventDispatcher           = eventDispatcher;
        this.stampManagedEntity        = stampManagedEntity;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @Override
    public void lock( final Object entity, final int type ) {
        assert entity != null : "variable entity should not be null";

        this.entityManager.lock( entity, DataStorageImpl.LOCK_TYPE.get( type ) );
    }


    @Override
    public void persist( final Object entity ) {
        assert entity != null : "variable entity should not be null";

        stampManagedEntity.buffer( entity );
    }


    @Override
    public void remove( final Object entity ) {
        assert entity != null : "variable entity should not be null";

        stampManagedEntity.remove( entity );
    }


    @Override
    public void save() {
        stampManagedEntity.collectBuffer( entityManager );

        this.entityManager.flush();

        this.eventDispatcher.trigger( Event.TRANSACTION_SUCCESS, new HashMap<>() );

        stampManagedEntity.collectBuffer( entityManager );

        applicationEventPublisher.publishEvent( new ApplicationEvent( new Object() ) {
            @Override
            public Object getSource() {
                return null;
            }


            @Override
            public String toString() {
                return "TRANSACTION_COMMITED_EVENT";
            }
        } );
    }
}
