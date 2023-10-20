package com.fairfair.callcenter.system.repository.emergency.order.item.negotiated;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpNotFoundException;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class NegotiatedItemRepositoryImpl implements NegotiatedItemRepository {
    protected final TokenProvider tokenProvider;
    protected final Environment   environment;


    public NegotiatedItemRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< NegotiatedItem > findByDefaultItemIdAndPrescriber( long defaultItemId, long prescriberId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/negotiated_items/by/default_item/{defaultItemId}/and/prescriber/{prescriberId}" )
                        .routeParam( "defaultItemId", String.valueOf( defaultItemId ) )
                        .routeParam( "prescriberId", String.valueOf( prescriberId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        return response.isSuccess()
                ? Optional.of( NegotiatedItem.build( response.getBodyAsMap() ) )
                : Optional.empty();
    }


    @Override
    public NegotiatedItem findOrFailByDefaultItemIdAndPrescriber( long defaultItemId, long prescriberId ) {
        Optional< NegotiatedItem > negotiatedItemOptional = findByDefaultItemIdAndPrescriber( defaultItemId, prescriberId );

        return negotiatedItemOptional.orElseThrow(
                () -> new HttpNotFoundException( "NEGOTIATED_ITEM_NOT_FOUND" )
        );
    }
}
