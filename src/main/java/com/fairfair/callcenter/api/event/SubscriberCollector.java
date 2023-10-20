package com.fairfair.callcenter.api.event;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class SubscriberCollector {

    protected static SubscriberCollector     INSTANCE = null;
    protected final  List< EventSubscriber > eventSubscribers;


    public SubscriberCollector( List< EventSubscriber > eventSubscribers ) {
        if ( eventSubscribers == null ) {
            eventSubscribers = new ArrayList<>();
        }
        
        this.eventSubscribers = eventSubscribers;
        INSTANCE              = this;
    }
}
