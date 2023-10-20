package com.fairfair.callcenter.system.repository.emergency.order.address;

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
public class OrderAddressRepositoryImpl implements OrderAddressRepository {
    private final   Map< Long, Optional< OrderAddress > > CACHE = new HashMap<>();
    protected final TokenProvider                         tokenProvider;
    protected final Environment                           environment;


    public OrderAddressRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< OrderAddress > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/order_addresses/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< OrderAddress > orderAddressOptional = Optional.of( OrderAddress.build( response.getBodyAsMap() ) );

            CACHE.put( id, orderAddressOptional );

            return orderAddressOptional;
        }

        return Optional.empty();
    }


    @Override
    public OrderAddress findOrFail( long id ) {
        Optional< OrderAddress > orderAddressOptional = findById( id );

        return orderAddressOptional.orElseThrow(
                () -> new HttpNotFoundException( "ORDER_ADDRESS_NOT_FOUND" )
        );
    }
}
