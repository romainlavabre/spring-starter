package com.fairfair.callcenter.system.repository.emergency.order.item.quotation;

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
public class QuotationItemRepositoryImpl implements QuotationItemRepository {
    private final   Map< Long, Optional< QuotationItem > > CACHE;
    protected final TokenProvider                          tokenProvider;
    protected final Environment                            environment;


    public QuotationItemRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
        CACHE              = new HashMap<>();
    }


    @Override
    public Optional< QuotationItem > findById( long id ) {
        if ( CACHE.containsKey( id ) ) {
            return CACHE.get( id );
        }

        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/quotation_items/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            Optional< QuotationItem > quotationOptional = Optional.of( QuotationItem.build( response.getBodyAsMap() ) );

            CACHE.put( id, quotationOptional );

            return quotationOptional;
        }

        return Optional.empty();
    }


    @Override
    public QuotationItem findOrFail( long id ) {
        Optional< QuotationItem > result = findById( id );

        return result.orElseThrow(
                () -> new HttpNotFoundException( "QUOTATION_ITEM_NOT_FOUND" )
        );
    }


    @Override
    public Optional< List< QuotationItem > > findByQuotation( long id ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/quotation_items/by/quotation/{id}" )
                        .routeParam( "id", String.valueOf( id ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();


        if ( response.isSuccess() ) {
            List< QuotationItem > quotationItems = new ArrayList<>();

            for ( Object data : response.getBodyAsList() ) {

                quotationItems.add( QuotationItem.build( ( ( JSONObject ) data ).toMap() ) );
            }

            return Optional.of( quotationItems );
        }

        return Optional.empty();
    }
}
