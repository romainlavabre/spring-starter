package com.fairfair.callcenter.system.repository.user.admin;

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
public class AdminRepositoryImpl implements AdminRepository {
    protected final TokenProvider                  tokenProvider;
    protected final Environment                    environment;
    private final   Map< Long, Optional< Admin > > CACHE = new HashMap<>();


    public AdminRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Admin > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/admins/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Admin > adminOptional = Optional.of( Admin.build( response.getBodyAsMap() ) );

            CACHE.put( id, adminOptional );

            return adminOptional;
        }

        return Optional.empty();
    }


    @Override
    public Admin findOrFailById( long id ) {
        Optional< Admin > adminOptional = findById( id );

        return adminOptional.orElseThrow(
                () -> new HttpNotFoundException( "ADMIN_NOT_FOUND" )
        );
    }
}
