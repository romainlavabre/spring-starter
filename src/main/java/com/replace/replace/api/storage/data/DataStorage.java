package com.replace.replace.api.storage.data;

import com.replace.replace.api.event.Event;
import com.replace.replace.api.event.EventDispatcher;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.HashMap;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class DataStorage implements DataStorageHandler {

    protected EntityManager   entityManager;
    protected EventDispatcher eventDispatcher;


    public DataStorage(
            final EntityManager entityManager,
            final EventDispatcher eventDispatcher ) {
        this.entityManager   = entityManager;
        this.eventDispatcher = eventDispatcher;
    }


    @Override
    public void persist( final Object entity ) {
        this.entityManager.persist( entity );
    }


    @Override
    public void remove( final Object entity ) {
        this.entityManager.remove( entity );
    }


    @Override
    public void save() {
        this.entityManager.flush();

        this.eventDispatcher.newEvent( Event.TRANSACTION_SUCCESS, new HashMap<>() );

        this.entityManager.flush();
    }

}
