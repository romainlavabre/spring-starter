package com.replace.replace.api.event;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface EventSubscriber {

    /**
     * Will receive event target where is launched
     *
     * @param event
     * @param params
     */
    void receiveEvent( String event, Map< String, Object > params )
            throws RuntimeException;
}
