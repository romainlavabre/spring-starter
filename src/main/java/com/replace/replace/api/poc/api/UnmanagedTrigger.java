package com.replace.replace.api.poc.api;

import com.replace.replace.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface UnmanagedTrigger {

    void handle( Request request, Object resource );
}
