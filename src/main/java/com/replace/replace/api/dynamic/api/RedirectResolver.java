package com.replace.replace.api.dynamic.api;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface RedirectResolver< T > {
    String getId();


    Object getResource( T subject );
}
