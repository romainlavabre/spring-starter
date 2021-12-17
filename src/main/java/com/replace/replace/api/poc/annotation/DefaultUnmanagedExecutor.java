package com.replace.replace.api.poc.annotation;

import com.replace.replace.api.poc.api.UnmanagedTrigger;
import com.replace.replace.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class DefaultUnmanagedExecutor implements UnmanagedTrigger {

    @Override
    public void handle( Request request, Object resource ) {

    }
}
