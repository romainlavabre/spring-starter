package com.fairfair.callcenter.system.repository.user.prescriber.referent;

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
public class ReferentRepositoryImpl implements ReferentRepository {
    private final   Map< Long, Optional< Referent > > CACHE = new HashMap<>();
    protected final TokenProvider                     tokenProvider;
    protected final Environment                       environment;


    public ReferentRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Referent > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/referents/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Referent > referentOptional = Optional.of( Referent.build( response.getBodyAsMap() ) );

            CACHE.put( id, referentOptional );

            return referentOptional;
        }

        return Optional.empty();
    }


    @Override
    public Referent findOrFail( Long id ) {
        if ( id == null ) {
            throw new HttpNotFoundException( "REFERENT_NOT_FOUND" );
        }

        Optional< Referent > referentOptional = findById( id );

        return referentOptional.orElseThrow(
                () -> new HttpNotFoundException( "REFERENT_NOT_FOUND" )
        );
    }
}
