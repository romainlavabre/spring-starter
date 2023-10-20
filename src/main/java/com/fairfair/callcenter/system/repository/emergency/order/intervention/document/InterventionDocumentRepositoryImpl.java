package com.fairfair.callcenter.system.repository.emergency.order.intervention.document;

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
public class InterventionDocumentRepositoryImpl implements InterventionDocumentRepository {
    private final   Map< Long, Optional< InterventionDocument > > CACHE = new HashMap<>();
    protected final TokenProvider                                 tokenProvider;
    protected final Environment                                   environment;


    public InterventionDocumentRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< InterventionDocument > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/intervention_documents/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< InterventionDocument > interventionOptional = Optional.of( InterventionDocument.build( response.getBodyAsMap() ) );

            CACHE.put( id, interventionOptional );

            return interventionOptional;
        }

        return Optional.empty();
    }


    @Override
    public InterventionDocument findOrFail( long id ) {
        Optional< InterventionDocument > interventionOptional = findById( id );

        return interventionOptional.orElseThrow(
                () -> new HttpNotFoundException( "INTERVENTION_DOCUMENT_NOT_FOUND" )
        );
    }


    @Override
    public Optional< InterventionDocument > findByInterventionId( long id ) {

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/intervention_documents/by/intervention/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< InterventionDocument > interventionOptional = Optional.of( InterventionDocument.build( response.getBodyAsMap() ) );

            return interventionOptional;
        }

        return Optional.empty();
    }


    @Override
    public InterventionDocument findOrFailByInterventionId( long id ) {
        Optional< InterventionDocument > interventionDocumentOptional = findByInterventionId( id );

        return interventionDocumentOptional.orElseThrow(
                () -> new HttpNotFoundException( "INTERVENTION_DOCUMENT_NOT_FOUND" )
        );
    }
}
