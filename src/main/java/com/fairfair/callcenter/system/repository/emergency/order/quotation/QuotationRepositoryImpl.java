package com.fairfair.callcenter.system.repository.emergency.order.quotation;

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
public class QuotationRepositoryImpl implements QuotationRepository {
    private final   Map< Long, Optional< Quotation > > CACHE = new HashMap<>();
    protected final TokenProvider                      tokenProvider;
    protected final Environment                        environment;


    public QuotationRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Quotation > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/quotations/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Quotation > quotationOptional = Optional.of( Quotation.build( response.getBodyAsMap() ) );

            CACHE.put( id, quotationOptional );

            return quotationOptional;
        }

        return Optional.empty();
    }


    @Override
    public Quotation findOrFail( long id ) {
        Optional< Quotation > quotationOptional = findById( id );

        return quotationOptional.orElseThrow(
                () -> new HttpNotFoundException( "QUOTATION_NOT_FOUND" )
        );
    }


    @Override
    public Optional< Quotation > findByInterventionId( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/quotations/by/intervention/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        return response.isSuccess()
                ? Optional.of( Quotation.build( response.getBodyAsMap() ) )
                : Optional.empty();
    }


    @Override
    public Quotation findOrFailByInterventionId( long id ) {
        Optional< Quotation > quotationOptional = findByInterventionId( id );

        return quotationOptional.orElseThrow(
                () -> new HttpNotFoundException( "QUOTATION_NOT_FOUND" )
        );
    }
}
