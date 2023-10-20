package com.fairfair.callcenter.system.repository.user.work;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.exception.HttpNotFoundException;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import com.fairfair.callcenter.system.repository.user.company.Company;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class WorkRepositoryImpl implements WorkRepository {
    protected final TokenProvider                 tokenProvider;
    protected final Environment                   environment;
    private final   Map< Long, Optional< Work > > CACHE = new HashMap<>();


    public WorkRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Work > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/works/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Work > workOptional = Optional.of( Work.build( response.getBodyAsMap() ) );

            CACHE.put( id, workOptional );

            return workOptional;
        }

        return Optional.empty();
    }


    @Override
    public Work findOrFail( long id ) {
        Optional< Work > workOptional = findById( id );

        return workOptional.orElseThrow(
                () -> new HttpNotFoundException( "WORK_NOT_FOUND" )
        );
    }


    @Override
    public List< Work > findAllByCompany( Company company ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/works/by/company/{id}" )
                        .routeParam( "id", String.valueOf( company.getId() ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();


        List< Work > works = new ArrayList<>();

        for ( Object object : response.getBodyAsList() ) {
            works.add( Work.build( ( ( JSONObject ) object ).toMap() ) );
        }

        return works;
    }
}
