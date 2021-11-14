package com.replace.replace.api.poc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@ResponseStatus( code = HttpStatus.NOT_FOUND )
public class HttpNotFoundException extends RuntimeException {
    public HttpNotFoundException() {
        super( "ROUTE_NOT_FOUND" );
    }
}
