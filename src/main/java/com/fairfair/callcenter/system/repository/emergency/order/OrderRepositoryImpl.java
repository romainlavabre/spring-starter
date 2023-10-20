package com.fairfair.callcenter.system.repository.emergency.order;

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
public class OrderRepositoryImpl implements OrderRepository {
    protected final TokenProvider tokenProvider;
    protected final Environment   environment;


    public OrderRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Order > findById( long id ) {

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/orders/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Order > orderOptional = Optional.of( Order.build( response.getBodyAsMap() ) );

            return orderOptional;
        }

        return Optional.empty();
    }


    @Override
    public Order findOrFail( long id ) {
        Optional< Order > orderOptional = findById( id );

        return orderOptional.orElseThrow(
                () -> new HttpNotFoundException( "ORDER_NOT_FOUND" )
        );
    }


    @Override
    public Optional< Order > findByIntervention( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/orders/by/intervention/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        return response.isSuccess()
                ? Optional.of( Order.build( response.getBodyAsMap() ) )
                : Optional.empty();
    }


    @Override
    public Order findOrFailByIntervention( long id ) {
        Optional< Order > orderOptional = findByIntervention( id );

        return orderOptional.orElseThrow(
                () -> new HttpNotFoundException( "ORDER_NOT_FOUND" )
        );
    }


    @Override
    public boolean isPrescriber( long orderId, long prescriberId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/orders/{orderId}/is/prescriber/{prescriberId}" )
                        .routeParam( "orderId", String.valueOf( orderId ) )
                        .routeParam( "prescriberId", String.valueOf( prescriberId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( !response.isSuccess() ) {
            return false;
        }

        return ( Boolean ) response.getBodyAsMap().get( "result" );
    }
}
