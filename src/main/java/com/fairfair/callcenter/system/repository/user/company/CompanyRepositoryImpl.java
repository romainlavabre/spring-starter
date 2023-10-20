package com.fairfair.callcenter.system.repository.user.company;

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
public class CompanyRepositoryImpl implements CompanyRepository {

    protected final TokenProvider                    tokenProvider;
    protected final Environment                      environment;
    private final   Map< Long, Optional< Company > > CACHE = new HashMap<>();


    public CompanyRepositoryImpl(
            TokenProvider tokenProvider,
            Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public Optional< Company > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/companies/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Company > companyOptional = Optional.of( Company.build( response.getBodyAsMap() ) );

            CACHE.put( id, companyOptional );

            return companyOptional;
        }

        return Optional.empty();
    }


    @Override
    public Company findOrFail( long id ) {
        Optional< Company > companyOptional = findById( id );

        return companyOptional.orElseThrow(
                () -> new HttpNotFoundException( "COMPANY_NOT_FOUND" )
        );
    }


    @Override
    public Optional< Company > findByEntrepreneurId( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/companies/by/entrepreneur/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< Company > companyOptional = Optional.of( Company.build( response.getBodyAsMap() ) );

            CACHE.put( id, companyOptional );

            return companyOptional;
        }

        return Optional.empty();
    }


    @Override
    public Company findOrFailByEntrepreneur( long id ) {
        Optional< Company > optionalCompany = findByEntrepreneurId( id );

        if ( optionalCompany.isPresent() ) {
            return optionalCompany.get();
        }

        throw new HttpNotFoundException( "COMPANY_NOT_FOUND" );
    }


    @Override
    public Company findOrFailByBillingId( long billingId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/companies/by/billing_id/{id}" )
                        .routeParam( "id", String.valueOf( billingId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Company.build( response.getBodyAsMap() );
        }

        throw new HttpNotFoundException( "COMPANY_NOT_FOUND" );
    }


    @Override
    public Company findOrFailByArtisan( long artisanId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/companies/by/artisan/{id}" )
                        .routeParam( "id", String.valueOf( artisanId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Company.build( response.getBodyAsMap() );
        }

        throw new HttpNotFoundException( "COMPANY_NOT_FOUND" );
    }


    @Override
    public Optional< Company > findByFairfairToolsCustomerId( long customerId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.user.url" ) + "/system/companies/by/fairfair_tools_customer_id/{id}" )
                        .routeParam( "id", String.valueOf( customerId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            return Optional.of( Company.build( response.getBodyAsMap() ) );
        }

        return Optional.empty();
    }


    @Override
    public Company findOrFailByFairfairToolsCustomerId( long customerId ) {
        Optional< Company > companyOptional = findByFairfairToolsCustomerId( customerId );

        return companyOptional
                .orElseThrow( () -> new HttpNotFoundException( "COMPANY_NOT_FOUND" ) );
    }
}
