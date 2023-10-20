package com.fairfair.callcenter.system.repository.emergency.order.document;

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
public class OrderDocumentRepositoryImpl implements OrderDocumentRepository {
    private final   Map< Long, Optional< OrderDocument > > CACHE = new HashMap<>();
    protected final TokenProvider                          tokenProvider;
    protected final Environment                            environment;


    public OrderDocumentRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< OrderDocument > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/order_documents/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< OrderDocument > orderDocumentOptional = Optional.of( OrderDocument.build( response.getBodyAsMap() ) );

            CACHE.put( id, orderDocumentOptional );

            return orderDocumentOptional;
        }

        return Optional.empty();
    }


    @Override
    public OrderDocument findOrFail( long id ) {
        Optional< OrderDocument > orderDocumentOptional = findById( id );

        return orderDocumentOptional.orElseThrow(
                () -> new HttpNotFoundException( "ORDER_DOCUMENT_NOT_FOUND" )
        );
    }


    @Override
    public Optional< OrderDocument > findByOrder( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/order_documents/by/order/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        return response.isSuccess()
                ? Optional.of( OrderDocument.build( response.getBodyAsMap() ) )
                : Optional.empty();
    }


    @Override
    public OrderDocument findOrFailByOrder( long id ) {
        Optional< OrderDocument > orderDocumentOptional = findByOrder( id );

        return orderDocumentOptional.orElseThrow(
                () -> new HttpNotFoundException( "ORDER_DOCUMENT_NOT_FOUND" )
        );
    }
}
