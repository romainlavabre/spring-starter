package com.replace.replace.api.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventDispatcherImpl implements EventDispatcher {

    protected final Map< String, List< EventSubscriber > > subscribers;
    protected final EventRepository                        eventRepository;
    protected       Logger                                 logger = LoggerFactory.getLogger( this.getClass() );

    public EventDispatcherImpl( EventRepository eventRepository ) {
        this.subscribers     = new HashMap<>();
        this.eventRepository = eventRepository;
    }

    @Override
    public EventDispatcher follow( String event, EventSubscriber eventSubscriber ) {

        this.logger.info( eventSubscriber.getClass().getName() + " as subscribed to event \"" + event + "\"" );

        if ( !this.isInitializedEvent( event ) ) {
            this.initializeEventSubscribers( event );
        }

        if ( !this.subscribers.get( event ).contains( eventSubscriber ) ) {
            this.subscribers.get( event ).add( eventSubscriber );
        }

        return this;
    }


    @Override
    public EventDispatcher newEvent( String event, Map< String, Object > params ) {

        this.eventRepository.isValidCredentials( event, params );


        List< EventSubscriber > calledClass = new ArrayList<>();

        if ( this.eventRepository.hasDefaultSubscribers( event ) ) {
            for ( EventSubscriber eventSubscriber : this.eventRepository.getDefaultSubscribers( event ) ) {
                eventSubscriber.receiveEvent( event, params );

                calledClass.add( eventSubscriber );
            }
        }

        if ( this.isInitializedEvent( event ) ) {
            for ( EventSubscriber eventSubscriber : this.subscribers.get( event ) ) {
                if ( calledClass.contains( eventSubscriber ) ) {
                    continue;
                }

                eventSubscriber.receiveEvent( event, params );
            }
        }

        return this;
    }

    /**
     * Control that event is already initialized in subscribers repository
     */
    protected boolean isInitializedEvent( String event ) {
        return this.subscribers.containsKey( event );
    }

    /**
     * Initialize event in subscribers repository
     */
    protected void initializeEventSubscribers( String event ) {

        this.subscribers.put( event, new ArrayList<>() );
    }
}
