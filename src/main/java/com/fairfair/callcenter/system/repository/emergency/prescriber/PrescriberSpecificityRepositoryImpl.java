package com.fairfair.callcenter.system.repository.emergency.prescriber;

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
public class PrescriberSpecificityRepositoryImpl implements PrescriberSpecificityRepository {

    protected final TokenProvider tokenProvider;
    protected final Environment   environment;


    public PrescriberSpecificityRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< PrescriberSpecificity > findById( long id ) {

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/prescriber_specificities/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( PrescriberSpecificity.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public PrescriberSpecificity findOrFail( long id ) {
        Optional< PrescriberSpecificity > prescriberSpecificityOptional = findById( id );

        return prescriberSpecificityOptional.orElseThrow(
                () -> new HttpNotFoundException( "PRESCRIBER_SPECIFICITY_NOT_FOUND" )
        );
    }


    @Override
    public Optional< PrescriberSpecificity > findByPrescriberId( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/prescriber_specificities/by/prescriber/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( PrescriberSpecificity.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public PrescriberSpecificity findOrFailByPrescriberId( long id ) {
        Optional< PrescriberSpecificity > prescriberSpecificityOptional = findByPrescriberId( id );

        return prescriberSpecificityOptional.orElseThrow(
                () -> new HttpNotFoundException( "PRESCRIBER_SPECIFICITY_NOT_FOUND" )
        );
    }
}
