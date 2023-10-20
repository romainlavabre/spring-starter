package com.fairfair.callcenter.system.repository.emergency.order.intervention.document.requested;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpNotFoundException;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class RequestedDocumentRepositoryImpl implements RequestedDocumentRepository {
    private final   Map< Long, Optional< RequestedDocument > > CACHE = new HashMap<>();
    protected final TokenProvider                              tokenProvider;
    protected final Environment                                environment;


    public RequestedDocumentRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< RequestedDocument > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/requested_documents/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< RequestedDocument > requestedDocumentOptional = Optional.of( RequestedDocument.build( response.getBodyAsMap() ) );

            CACHE.put( id, requestedDocumentOptional );

            return requestedDocumentOptional;
        }

        return Optional.empty();
    }


    @Override
    public RequestedDocument findOrFail( long id ) {
        return findById( id )
                .orElseThrow( () -> new HttpNotFoundException( "REQUESTED_DOCUMENT_NOT_FOUND" ) );
    }


    @Override
    public List< RequestedDocument > findAllByInterventionId( long interventionId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/requested_documents/by/intervention/{id}" )
                        .routeParam( "id", String.valueOf( interventionId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            List< RequestedDocument > requestedDocuments = new ArrayList<>();

            for ( Object data : response.getBodyAsList() ) {
                requestedDocuments.add( RequestedDocument.build( ( ( JSONObject ) data ).toMap() ) );
            }

            return requestedDocuments;
        }

        return new ArrayList<>();
    }
}
