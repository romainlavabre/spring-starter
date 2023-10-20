package com.fairfair.callcenter.system.repository.authentication.identity;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.configuration.environment.Variable;
import com.fairfair.callcenter.exception.HttpNotFoundException;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "SystemIdentityRepository" )
public class IdentityRepositoryImpl implements IdentityRepository {

    protected final TokenProvider tokenProvider;
    protected final Environment   environment;


    public IdentityRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Identity > findById( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( Variable.SERVICE_AUTH_URL ) + "/system/identities/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Identity.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public Identity findOrFail( long id ) {
        Optional< Identity > identityOptional = findById( id );

        return identityOptional.orElseThrow(
                () -> new HttpNotFoundException( "IDENTITY_NOT_FOUND" )
        );
    }


    @Override
    public Optional< Identity > findByUsername( String username ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( Variable.SERVICE_AUTH_URL ) + "/system/identities/{username}" )
                        .routeParam( "username", username )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Identity.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public Identity findOrFailByUsername( String username ) {
        return findByUsername( username )
                .orElseThrow( () -> new HttpNotFoundException( "IDENTITY_NOT_FOUND" ) );
    }
}
