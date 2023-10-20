package com.fairfair.callcenter.system.repository.authentication;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpInternalServerErrorException;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Base64;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class TokenProviderImpl implements TokenProvider {

    protected final Environment environment;
    private         String      token;
    private         Long        expireAt;


    public TokenProviderImpl( Environment environment ) {
        this.environment = environment;
    }


    @Override
    public String getToken() {
        if ( isValidToken() ) {
            return token;
        }

        return requestToken();
    }


    @Override
    public String getToken( boolean forceRefresh ) {
        if ( forceRefresh ) {
            return requestToken();
        }

        return getToken();
    }


    /**
     * @return TRUE if expiration date (-30 seconds) is superior of actual date
     */
    protected boolean isValidToken() {
        long now = ZonedDateTime.now( ZoneId.of( "UTC" ) ).toEpochSecond();

        return token != null && (expireAt - 30) > now;
    }


    /**
     * Request token to authentication server and set expiration date and token for next usage
     * WARNING: This method is not stateless so must be synchronized
     *
     * @return Token
     * @throws HttpInternalServerErrorException
     */
    private synchronized String requestToken() throws HttpInternalServerErrorException {
        Response response = Rest.builder()
                                .init( RequestBuilder.POST, environment.getEnv( "service.auth.url" ) + "/auth/token" )
                                .field( "grant_type", "client_credentials" )
                                .field( "client_id", environment.getEnv( "auth.client-id" ) )
                                .field( "client_secret", environment.getEnv( "auth.client-secret" ) )
                                .inContentType( RequestBuilder.FORM_URL_ENCODED )
                                .buildAndSend();

        if ( response.status() != 200 ) {
            throw new HttpInternalServerErrorException( "SYSTEM_AUTHENTICATION_ERROR" );
        }

        String encodedToken = response.getBodyAsMap().get( "access_token" ).toString();

        String token = new String( Base64.getDecoder().decode( encodedToken.split( "\\." )[ 1 ] ), StandardCharsets.UTF_8 );

        JSONObject jsonObject = new JSONObject( token );

        expireAt = Long.valueOf( jsonObject.get( "exp" ).toString() );

        return this.token = encodedToken;
    }
}
