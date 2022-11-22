package com.replace.replace.api.storage.data;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class StampManagedEntity {

    private final List< Object > persistedEntities;
    private final List< Object > removedEntities;


    public StampManagedEntity() {
        persistedEntities = new ArrayList<>();
        removedEntities   = new ArrayList<>();
    }


    public void buffer( Object entity ) {
        if ( !persistedEntities.contains( entity ) ) {
            persistedEntities.add( entity );
        }
    }


    public void remove( Object entity ) {
        persistedEntities.remove( entity );

        removedEntities.add( entity );
    }


    public void collectBuffer( EntityManager entityManager ) {

        Iterator< Object > iteratorPersistedEntities = persistedEntities.iterator();

        while ( iteratorPersistedEntities.hasNext() ) {
            Object entity = iteratorPersistedEntities.next();

            entityManager.persist( entity );

            iteratorPersistedEntities.remove();
        }

        Iterator< Object > iteratorRemovedEntities = removedEntities.iterator();

        while ( iteratorRemovedEntities.hasNext() ) {
            Object entity = iteratorRemovedEntities.next();

            entityManager.remove( entity );

            iteratorRemovedEntities.remove();
        }
    }
}
