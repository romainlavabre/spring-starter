package com.fairfair.callcenter.system.repository.emergency.order.client;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpNotFoundException;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class ClientRepositoryImpl implements ClientRepository {
    private final   Map< Long, Optional< Client > > CACHE = new HashMap<>();
    protected final TokenProvider                   tokenProvider;
    protected final Environment                     environment;


    public ClientRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Client > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/clients/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Client > clientOptional = Optional.of( Client.build( response.getBodyAsMap() ) );

            CACHE.put( id, clientOptional );

            return clientOptional;
        }

        return Optional.empty();
    }


    @Override
    public Client findOrFail( long id ) {
        Optional< Client > clientOptional = findById( id );

        return clientOptional.orElseThrow(
                () -> new HttpNotFoundException( "CLIENT_NOT_FOUND" )
        );
    }
}
