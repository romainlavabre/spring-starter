package com.fairfair.callcenter.api.crud;

import com.fairfair.callcenter.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Create< E > {
    void create( Request request, E entity );
}
