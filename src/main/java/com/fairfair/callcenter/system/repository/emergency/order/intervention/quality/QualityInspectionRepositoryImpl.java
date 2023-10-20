package com.fairfair.callcenter.system.repository.emergency.order.intervention.quality;

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
public class QualityInspectionRepositoryImpl implements QualityInspectionRepository {
    private final   Map< Long, Optional< QualityInspection > > CACHE = new HashMap<>();
    protected final TokenProvider                              tokenProvider;
    protected final Environment                                environment;


    public QualityInspectionRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< QualityInspection > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/quality_inspections/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< QualityInspection > interventionOptional = Optional.of( QualityInspection.build( response.getBodyAsMap() ) );

            CACHE.put( id, interventionOptional );

            return interventionOptional;
        }

        return Optional.empty();
    }


    @Override
    public QualityInspection findOrFail( long id ) {
        Optional< QualityInspection > interventionOptional = findById( id );

        return interventionOptional.orElseThrow(
                () -> new HttpNotFoundException( "QUALITY_INSPECTION_NOT_FOUND" )
        );
    }
}
