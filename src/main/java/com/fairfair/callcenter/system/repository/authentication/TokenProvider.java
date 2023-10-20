package com.fairfair.callcenter.system.repository.authentication;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface TokenProvider {
    /**
     * @return Token for connection
     */
    String getToken();


    /**
     * @return Token for connection
     */
    String getToken( boolean forceRefresh );
}
