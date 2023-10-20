package com.fairfair.callcenter.system.repository.user.company.artisan;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
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
public class ArtisanRepositoryImpl implements ArtisanRepository {
    protected final TokenProvider                    tokenProvider;
    protected final Environment                      environment;
    private final   Map< Long, Optional< Artisan > > CACHE = new HashMap<>();


    public ArtisanRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Artisan > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/artisans/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Artisan > artisanOptional = Optional.of( Artisan.build( response.getBodyAsMap() ) );

            CACHE.put( id, artisanOptional );

            return artisanOptional;
        }

        return Optional.empty();
    }


    @Override
    public Artisan findOrFail( long id ) {
        Optional< Artisan > artisanOptional = findById( id );

        return artisanOptional.orElseThrow(
                () -> new HttpNotFoundException( "ARTISAN_NOT_FOUND" )
        );
    }


    @Override
    public Optional< List< Artisan > > findAllByCompany( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/artisans/by/company/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();


        if ( response.isSuccess() ) {
            List< Artisan > artisans = new ArrayList<>();
            for ( Object object : response.getBodyAsList() ) {
                JSONObject artisanEncoded = ( JSONObject ) object;

                artisans.add( Artisan.build( artisanEncoded.toMap() ) );
            }

            return Optional.of( artisans );
        }


        return Optional.empty();
    }


    @Override
    public List< Artisan > findOrFailAllByCompany( long id ) {
        Optional< List< Artisan > > optionalArtisans = findAllByCompany( id );

        return optionalArtisans.orElseThrow(
                () -> new HttpNotFoundException( "COMPANY_NOT_FOUND" )
        );
    }
}
