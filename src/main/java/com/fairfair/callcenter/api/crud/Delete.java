package com.fairfair.callcenter.api.crud;

import com.fairfair.callcenter.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Delete< E > {
    void delete( Request request, E entity );
}
