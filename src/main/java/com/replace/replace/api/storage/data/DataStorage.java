package com.replace.replace.api.storage.data;

import com.replace.replace.api.event.Event;
import com.replace.replace.api.event.EventDispatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DataStorage implements DataStorageHandler {

    protected static final Map< Integer, LockModeType > LOCK_TYPE = Map.of(
            4, LockModeType.PESSIMISTIC_READ,
            5, LockModeType.PESSIMISTIC_WRITE
    );
    protected              EntityManager                entityManager;
    protected              EventDispatcher              eventDispatcher;


    public DataStorage(
            final EntityManager entityManager,
            final EventDispatcher eventDispatcher ) {
        this.entityManager   = entityManager;
        this.eventDispatcher = eventDispatcher;
    }


    @Override
    public void lock( final Object entity, final int type ) {
        assert entity != null : "variable entity should not be null";

        this.entityManager.lock( entity, DataStorage.LOCK_TYPE.get( type ) );
    }


    @Override
    public void persist( final Object entity ) {
        assert entity != null : "variable entity should not be null";

        this.entityManager.persist( entity );
    }


    @Override
    public void remove( final Object entity ) {
        assert entity != null : "variable entity should not be null";
        
        this.entityManager.remove( entity );
    }


    @Override
    public void save() {
        this.entityManager.flush();

        this.eventDispatcher.newEvent( Event.TRANSACTION_SUCCESS, new HashMap<>() );
    }
}
