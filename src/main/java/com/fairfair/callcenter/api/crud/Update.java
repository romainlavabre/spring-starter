package com.fairfair.callcenter.api.crud;

import com.fairfair.callcenter.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Update< E > {
    void update( Request request, E entity );
}
