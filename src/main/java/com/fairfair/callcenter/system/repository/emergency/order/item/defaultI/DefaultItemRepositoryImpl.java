package com.fairfair.callcenter.system.repository.emergency.order.item.defaultI;

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
public class DefaultItemRepositoryImpl implements DefaultItemRepository {
    private final   Map< Long, Optional< DefaultItem > > CACHE = new HashMap<>();
    protected final TokenProvider                        tokenProvider;
    protected final Environment                          environment;


    public DefaultItemRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< DefaultItem > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/default_items/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< DefaultItem > defaultItemOptional = Optional.of( DefaultItem.build( response.getBodyAsMap() ) );

            CACHE.put( id, defaultItemOptional );

            return defaultItemOptional;
        }

        return Optional.empty();
    }


    @Override
    public DefaultItem findOrFail( long id ) {
        Optional< DefaultItem > defaultItemOptional = findById( id );

        return defaultItemOptional.orElseThrow(
                () -> new HttpNotFoundException( "DEFAULT_ITEM_NOT_FOUND" )
        );
    }
}
