package com.fairfair.callcenter.system.repository.user.company.address;

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
public class CompanyAddressRepositoryImpl implements CompanyAddressRepository {

    protected final TokenProvider                           tokenProvider;
    protected final Environment                             environment;
    private final   Map< Long, Optional< CompanyAddress > > CACHE = new HashMap<>();


    public CompanyAddressRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< CompanyAddress > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/company_addresses/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< CompanyAddress > companyAddressOptional = Optional.of( CompanyAddress.build( response.getBodyAsMap() ) );

            CACHE.put( id, companyAddressOptional );

            return companyAddressOptional;
        }

        return Optional.empty();
    }


    @Override
    public CompanyAddress findOrFail( long id ) {
        Optional< CompanyAddress > companyAddressOptional = findById( id );

        return companyAddressOptional.orElseThrow(
                () -> new HttpNotFoundException( "COMPANY_ADDRESS_NOT_FOUND" )
        );
    }
}
