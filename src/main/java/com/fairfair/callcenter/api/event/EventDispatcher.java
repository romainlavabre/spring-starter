package com.fairfair.callcenter.api.event;


import com.fairfair.callcenter.configuration.event.Event;

import java.util.Map;

/**
 * @author Romain Lavabre <romain.lavabre@fairfair.com>
 * {@see https://github.com/romainlavabre/spring-starter-event.git}
 */
public interface EventDispatcher {

    /**
     * Launch new event
     *
     * @param event
     * @param params
     * @return
     */
    EventDispatcher trigger( Event event, Map< String, Object > params );
}
