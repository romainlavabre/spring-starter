package com.fairfair.callcenter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( code = HttpStatus.UNPROCESSABLE_ENTITY )
public class HttpUnprocessableEntityException extends RuntimeException {
    public HttpUnprocessableEntityException( String message ) {
        super( message );
    }
}
