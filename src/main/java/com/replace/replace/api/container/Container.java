package com.replace.replace.api.container;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Container {
    Object getInstance( String classname );


    < T > T getInstance( Class< T > type );
}
