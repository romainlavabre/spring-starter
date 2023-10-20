package com.fairfair.callcenter.api.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fairfair.callcenter.api.request.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class SecurityImpl implements Security {

    protected final Request               request;
    protected       Map< String, Object > body;


    public SecurityImpl( Request request ) {
        this.request = request;
        body         = null;

        String authorization = request.getHeader( "Authorization" );

        if ( authorization == null || !authorization.startsWith( "Bearer " ) ) {
            return;
        }

        try {
            body = new ObjectMapper().readValue( new String( Base64.getDecoder().decode( authorization.split( "\\." )[ 1 ] ), StandardCharsets.UTF_8 ), Map.class );
        } catch ( JsonProcessingException e ) {
            e.printStackTrace();
        }
    }


    @Override
    public long getAuthenticationId() {
        return Long.valueOf( body.get( "sub" ).toString() );
    }


    @Override
    public long getId() {
        if ( hasAttribute( "external_id" ) ) {
            return Long.valueOf( getAttribute( "external_id" ).toString() );
        }

        return -1;
    }


    @Override
    public String getUsername() {
        if ( body.get( "preferred_username" ) != null ) {
            return body.get( "preferred_username" ).toString();
        }

        return null;
    }


    @Override
    public Collection< String > getRoles() {
        return ( Collection< String > ) body.get( "roles" );
    }


    @Override
    public boolean hasRole( final String role ) {
        return getRoles().contains( role );
    }


    @Override
    public boolean hasUserConnected() {
        return body != null && body.get( "sub" ) != null;
    }


    @Override
    public String[] getScopes() {
        return body.get( "scope" ).toString().split( " " );
    }


    @Override
    public boolean hasScope( String scope ) {
        for ( String localScope : getScopes() ) {
            if ( localScope.toLowerCase().equals( scope.toLowerCase() ) ) {
                return true;
            }
        }

        return false;
    }


    @Override
    public String getClientId() {
        return body.get( "azp" ).toString();
    }


    @Override
    public boolean hasAttribute( String attribute ) {
        return (( Map< String, Object > ) body.get( "attributes" )).containsKey( attribute );
    }


    @Override
    public Object getAttribute( String attribute ) {
        return (( Map< String, Object > ) body.get( "attributes" )).get( attribute );
    }


    @Override
    public boolean has( String claim ) {
        return body.containsKey( claim );
    }


    @Override
    public Object get( String claim ) {
        return body.get( claim );
    }
}
