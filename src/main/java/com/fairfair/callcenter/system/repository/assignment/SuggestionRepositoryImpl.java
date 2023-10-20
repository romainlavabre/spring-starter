package com.fairfair.callcenter.system.repository.assignment;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.configuration.environment.Variable;
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
public class SuggestionRepositoryImpl implements SuggestionRepository {

    protected final      Environment                         environment;
    protected final      TokenProvider                       tokenProvider;
    private final static Map< Long, Optional< Suggestion > > CACHE = new HashMap<>();


    public SuggestionRepositoryImpl(
            Environment environment,
            TokenProvider tokenProvider ) {
        this.environment = environment;
        this.tokenProvider = tokenProvider;
    }


    @Override
    public Optional< Suggestion > findById( Long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( Variable.SERVICE_ASSIGNMENT_URL ) + "/system/suggestions/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Suggestion > suggestion = Optional.of( Suggestion.build( response.getBodyAsMap() ) );

            CACHE.put( id, suggestion );

            return suggestion;
        }

        return response.isSuccess()
                ? Optional.of( Suggestion.build( response.getBodyAsMap() ) )
                : Optional.empty();
    }


    @Override
    public Suggestion findOrFail( Long id ) {
        Optional< Suggestion > suggestionOptional = findById( id );

        return suggestionOptional
                .orElseThrow( () -> new HttpNotFoundException( "SUGGESTION_NOT_FOUND" ) );
    }


    @Override
    public Optional< Suggestion > findByToken( String token ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( Variable.SERVICE_ASSIGNMENT_URL ) + "/system/suggestions/by/token/{token}" )
                        .routeParam( "token", token )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        return response.isSuccess()
                ? Optional.of( Suggestion.build( response.getBodyAsMap() ) )
                : Optional.empty();
    }


    @Override
    public Suggestion findOrFailByToken( String token ) {
        Optional< Suggestion > suggestionOptional = findByToken( token );

        return suggestionOptional
                .orElseThrow( () -> new HttpNotFoundException( "SUGGESTION_NOT_FOUND" ) );
    }


    @Override
    public Optional< List< Suggestion > > findAllByOrder( Long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( Variable.SERVICE_ASSIGNMENT_URL ) + "/system/suggestions/by/order/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            List< Suggestion > result = new ArrayList<>();

            for ( Object unity : response.getBodyAsList() ) {
                result.add( Suggestion.build( ( (JSONObject) unity ).toMap() ) );
            }

            return Optional.of( result );
        }

        return Optional.empty();
    }

    @Override
    public List< Suggestion > findAllByB2cOrder( Long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( Variable.SERVICE_ASSIGNMENT_URL ) + "/system/suggestions/by/b2c_order/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            List< Suggestion > result = new ArrayList<>();

            for ( Object unity : response.getBodyAsList() ) {
                result.add( Suggestion.build( ( (JSONObject) unity ).toMap() ) );
            }

            return result;

        }

        return new ArrayList<>();
    }
}
