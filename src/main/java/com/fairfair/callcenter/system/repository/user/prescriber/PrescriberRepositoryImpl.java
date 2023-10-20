package com.fairfair.callcenter.system.repository.user.prescriber;

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
public class PrescriberRepositoryImpl implements PrescriberRepository {

    protected final TokenProvider                       tokenProvider;
    protected final Environment                         environment;
    private final   Map< Long, Optional< Prescriber > > CACHE = new HashMap<>();


    public PrescriberRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Prescriber > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/prescribers/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Prescriber > prescriberOptional = Optional.of( Prescriber.build( response.getBodyAsMap() ) );

            CACHE.put( id, prescriberOptional );

            return prescriberOptional;
        }

        return Optional.empty();
    }


    @Override
    public Prescriber findOrFail( long id ) {
        Optional< Prescriber > prescriberOptional = findById( id );

        return prescriberOptional.orElseThrow(
                () -> new HttpNotFoundException( "PRESCRIBER_NOT_FOUND" )
        );
    }


    @Override
    public Optional< Prescriber > findByReferentId( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/prescribers/by/referent/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Prescriber > prescriberOptional = Optional.of( Prescriber.build( response.getBodyAsMap() ) );

            CACHE.put( id, prescriberOptional );

            return prescriberOptional;
        }

        return Optional.empty();
    }


    @Override
    public Prescriber findOrFailByReferent( long id ) {
        Optional< Prescriber > optionalPrescriber = findByReferentId( id );

        if ( optionalPrescriber.isPresent() ) {
            return optionalPrescriber.get();
        }

        throw new HttpNotFoundException( "PRESCRIBER_NOT_FOUND" );
    }
}
