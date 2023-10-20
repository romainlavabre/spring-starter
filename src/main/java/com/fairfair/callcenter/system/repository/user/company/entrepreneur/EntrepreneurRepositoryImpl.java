package com.fairfair.callcenter.system.repository.user.company.entrepreneur;

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
public class EntrepreneurRepositoryImpl implements EntrepreneurRepository {
    protected final TokenProvider                         tokenProvider;
    protected final Environment                           environment;
    private final   Map< Long, Optional< Entrepreneur > > CACHE = new HashMap<>();


    public EntrepreneurRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Entrepreneur > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/entrepreneurs/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Entrepreneur > entrepreneurOptional = Optional.of( Entrepreneur.build( response.getBodyAsMap() ) );

            CACHE.put( id, entrepreneurOptional );

            return entrepreneurOptional;
        }

        return Optional.empty();
    }


    @Override
    public Entrepreneur findOrFail( Long id ) {
        if ( id == null ) {
            throw new HttpNotFoundException( "ENTREPRENEUR_NOT_FOUND" );
        }

        Optional< Entrepreneur > entrepreneurOptional = findById( id );

        return entrepreneurOptional.orElseThrow(
                () -> new HttpNotFoundException( "ENTREPRENEUR_NOT_FOUND" )
        );
    }


    @Override
    public Optional< Entrepreneur > findByEmail( String email ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/entrepreneurs/by/email/{email}" )
                        .routeParam( "email", email )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Entrepreneur.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public Optional< Entrepreneur > findByAuthenticationId( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/entrepreneurs/by/authentication_id/{authenticationId}" )
                        .routeParam( "authenticationId", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Entrepreneur.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public Entrepreneur findOrFailByAuthenticationId( long id ) {
        return findByAuthenticationId( id )
                .orElseThrow( () -> new HttpNotFoundException( "ENTREPRENEUR_NOT_FOUND" ) );
    }
}
