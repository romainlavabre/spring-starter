package com.fairfair.callcenter.api.event;

import com.fairfair.callcenter.configuration.event.Event;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventDispatcherImpl implements EventDispatcher {

    protected boolean isSorted = false;

    protected List< EventSubscriber > eventSubscribers;


    @Override
    public EventDispatcher trigger( final Event event, final Map< String, Object > params ) {
        if ( !isSorted ) {
            synchronized ( this ) {
                eventSubscribers = sortSubscribers( SubscriberCollector.INSTANCE.eventSubscribers );
            }
        }

        for ( final EventSubscriber eventSubscriber : eventSubscribers ) {
            if ( eventSubscriber.getEvents().contains( event ) ) {
                eventSubscriber.receiveEvent( event, params );
            }
        }

        return this;
    }


    protected List< EventSubscriber > sortSubscribers( final List< EventSubscriber > eventSubscribers ) {
        final List< EventSubscriber > result = new ArrayList<>();

        Map< Integer, List< EventSubscriber > > temporarySort = new HashMap<>();

        int maxPriority = 0;

        for ( final EventSubscriber eventSubscriber : eventSubscribers ) {
            if ( eventSubscriber.getPriority() > maxPriority ) {
                maxPriority = eventSubscriber.getPriority();
            }

            if ( temporarySort.containsKey( eventSubscriber.getPriority() ) ) {
                temporarySort.get( eventSubscriber.getPriority() ).add( eventSubscriber );
                continue;
            }

            final List< EventSubscriber > listByPriority = new ArrayList<>();
            listByPriority.add( eventSubscriber );

            temporarySort.put( eventSubscriber.getPriority(), listByPriority );
        }

        final Map< Integer, List< EventSubscriber > > temporarySubSort = new HashMap<>();

        for ( Map.Entry< Integer, List< EventSubscriber > > entry : temporarySort.entrySet() ) {
            List< String > classNames = new ArrayList<>();

            for ( EventSubscriber eventSubscriber : entry.getValue() ) {
                classNames.add( eventSubscriber.getClass().getName() );
            }

            Collections.sort( classNames );

            List< EventSubscriber > eventSubscribers1 = new ArrayList<>();

            for ( String className : classNames ) {
                for ( EventSubscriber eventSubscriber : entry.getValue() ) {
                    if ( className.equals( eventSubscriber.getClass().getName() ) ) {
                        eventSubscribers1.add( eventSubscriber );
                    }
                }
            }

            temporarySubSort.put( entry.getKey(), eventSubscribers1 );
        }

        temporarySort = temporarySubSort;

        for ( int i = 1; i <= maxPriority; i++ ) {
            if ( temporarySort.containsKey( i ) ) {
                result.addAll( temporarySort.get( i ) );
            }
        }

        if ( temporarySort.containsKey( 0 ) ) {
            result.addAll( temporarySort.get( 0 ) );
        }

        return result;
    }
}
