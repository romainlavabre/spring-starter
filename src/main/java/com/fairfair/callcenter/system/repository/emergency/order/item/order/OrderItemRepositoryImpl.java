package com.fairfair.callcenter.system.repository.emergency.order.item.order;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpNotFoundException;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class OrderItemRepositoryImpl implements OrderItemRepository {
    private final   Map< Long, Optional< OrderItem > > CACHE = new HashMap<>();
    protected final TokenProvider                      tokenProvider;
    protected final Environment                        environment;


    public OrderItemRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< OrderItem > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/order_items/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< OrderItem > orderItemOptional = Optional.of( OrderItem.build( response.getBodyAsMap() ) );

            CACHE.put( id, orderItemOptional );

            return orderItemOptional;
        }

        return Optional.empty();
    }


    @Override
    public OrderItem findOrFail( long id ) {
        Optional< OrderItem > orderItemOptional = findById( id );

        return orderItemOptional.orElseThrow(
                () -> new HttpNotFoundException( "ORDER_ITEM_NOT_FOUND" )
        );
    }


    @Override
    public Optional< List< OrderItem > > findByOrder( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/order_items/by/order/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();


        if ( response.isSuccess() ) {
            List< OrderItem > orderItems = new ArrayList<>();

            for ( Object data : response.getBodyAsList() ) {
                orderItems.add( OrderItem.build( ( ( JSONObject ) data ).toMap() ) );
            }

            return Optional.of( orderItems );
        }

        return Optional.empty();
    }
}
