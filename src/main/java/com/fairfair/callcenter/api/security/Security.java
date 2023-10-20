package com.fairfair.callcenter.api.security;

import java.util.Collection;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Security {
    long getId();


    long getAuthenticationId();


    String getUsername();


    Collection< String > getRoles();


    boolean hasRole( String role );


    boolean hasUserConnected();


    String[] getScopes();


    boolean hasScope( String scope );


    String getClientId();


    boolean hasAttribute( String attribute );


    Object getAttribute( String attribute );


    boolean has( String claim );


    Object get( String claim );
}
