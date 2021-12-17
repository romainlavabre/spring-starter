package com.replace.replace.api.poc.kernel.entry;

import com.replace.replace.api.poc.kernel.router.RouteHandler;
import com.replace.replace.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CreateEntry {
    void create( Request request, Object entity, RouteHandler.Route route )
            throws Throwable;
}
