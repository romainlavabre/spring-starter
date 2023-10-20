package com.fairfair.callcenter.api.datasource;

import com.fairfair.callcenter.api.request.Request;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ReadOnlyResolver {
    private static  ReadOnlyResolver       readOnlyResolver;
    protected final ThreadLocal< Boolean > readOnly = new ThreadLocal<>();
    protected final Request                request;


    private ReadOnlyResolver( Request request ) {
        this.request     = request;
        readOnlyResolver = this;
    }


    public static ReadOnlyResolver getInstance() {
        return readOnlyResolver;
    }


    public boolean isReadOnly() {
        return readOnly.get() != null && readOnly.get();
    }


    protected void setReadOnly() {
        readOnly.set( true );
    }


    protected void clear() {
        readOnly.set( false );
    }
}
