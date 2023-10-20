package com.fairfair.callcenter.system.event;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DataEventHandler {
    void created( Object entity );


    void updated( Object entity );


    void deleted( Object entity );
}
