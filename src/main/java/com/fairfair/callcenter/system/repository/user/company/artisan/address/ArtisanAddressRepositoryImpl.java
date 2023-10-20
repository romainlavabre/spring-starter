package com.fairfair.callcenter.system.repository.user.company.artisan.address;

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
public class ArtisanAddressRepositoryImpl implements ArtisanAddressRepository {
    private final   Map< Long, Optional< ArtisanAddress > > CACHE = new HashMap<>();
    protected final TokenProvider                           tokenProvider;
    protected final Environment                             environment;


    public ArtisanAddressRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< ArtisanAddress > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/artisan_addresses/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< ArtisanAddress > artisanAddressOptional = Optional.of( ArtisanAddress.build( response.getBodyAsMap() ) );

            CACHE.put( id, artisanAddressOptional );

            return artisanAddressOptional;
        }

        return Optional.empty();
    }


    @Override
    public ArtisanAddress findOrFail( long id ) {
        Optional< ArtisanAddress > artisanAddressOptional = findById( id );

        return artisanAddressOptional.orElseThrow(
                () -> new HttpNotFoundException( "ARTISAN_ADDRESS_NOT_FOUND" )
        );
    }
}
