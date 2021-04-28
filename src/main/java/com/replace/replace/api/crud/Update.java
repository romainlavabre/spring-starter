package com.replace.replace.api.crud;

import com.replace.replace.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Update< E > {
    void update( Request request, E entity );
}
