package com.fairfair.callcenter.system.repository.emergency.order.intervention;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpInternalServerErrorException;
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
public class InterventionRepositoryImpl implements InterventionRepository {
    private final   Map< Long, Optional< Intervention > > CACHE = new HashMap<>();
    protected final TokenProvider                         tokenProvider;
    protected final Environment                           environment;


    public InterventionRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Intervention > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/interventions/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Intervention > interventionOptional = Optional.of( Intervention.build( response.getBodyAsMap() ) );

            CACHE.put( id, interventionOptional );

            return interventionOptional;
        }

        return Optional.empty();
    }


    @Override
    public Intervention findOrFail( long id ) {
        Optional< Intervention > interventionOptional = findById( id );

        return interventionOptional.orElseThrow(
                () -> new HttpNotFoundException( "INTERVENTION_NOT_FOUND" )
        );
    }


    @Override
    public List< Intervention > findAllByOrder( long orderId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/interventions/by/order/{id}" )
                        .routeParam( "id", String.valueOf( orderId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            List< Intervention > interventions = new ArrayList<>();

            for ( Object data : response.getBodyAsList() ) {

                interventions.add( Intervention.build( ( ( JSONObject ) data ).toMap() ) );
            }

            return interventions;
        }

        return new ArrayList<>();
    }


    @Override
    public Optional< Intervention > findByOrderAndSuccess( long id ) {

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/interventions/success/by/order/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Intervention.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public Optional< Intervention > findByOrderAndCurrent( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/interventions/current/by/order/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Intervention.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public boolean isCompany( long interventionId, long companyId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/interventions/{interventionId}/is/company/{companyId}" )
                        .routeParam( "interventionId", String.valueOf( interventionId ) )
                        .routeParam( "companyId", String.valueOf( companyId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( !response.isSuccess() ) {
            return false;
        }

        return ( Boolean ) response.getBodyAsMap().get( "result" );
    }


    @Override
    public Optional< Map< String, Object > > getCompanyDebt( long companyId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/interventions/kpi/by/company/{id}" )
                        .routeParam( "id", String.valueOf( companyId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( !response.isSuccess() ) {
            throw new HttpInternalServerErrorException( "UNAVAILABLE_SERVER_DEPENDENCY" );
        }

        return Optional.of( response.getBodyAsMap() );
    }
}
