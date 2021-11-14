package com.replace.replace.api.poc.kernel.exception;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class NoRouteMatchException extends Throwable {

    public NoRouteMatchException() {
        super( "No route match with the current request" );
    }
}
