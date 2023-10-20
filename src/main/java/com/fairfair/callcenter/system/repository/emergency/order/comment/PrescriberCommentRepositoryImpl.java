package com.fairfair.callcenter.system.repository.emergency.order.comment;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Response;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import kong.unirest.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class PrescriberCommentRepositoryImpl implements PrescriberCommentRepository {
    protected final TokenProvider tokenProvider;
    protected final Environment   environment;


    public PrescriberCommentRepositoryImpl( TokenProvider tokenProvider, Environment environment ) {
        this.tokenProvider = tokenProvider;
        this.environment   = environment;
    }


    @Override
    public List< PrescriberComment > findAllByOrder( long orderId ) {
        Response response =
                Rest.builder()
                        .init( RequestBuilder.GET, environment.getEnv( "service.emergency.url" ) + "/system/prescriber_comments/by/order/{id}" )
                        .routeParam( "id", String.valueOf( orderId ) )
                        .addHeader( "Authorization", "Bearer " + tokenProvider.getToken() )
                        .buildAndSend();

        if ( response.isSuccess() ) {
            List< PrescriberComment > prescriberComments = new ArrayList<>();

            for ( Object data : response.getBodyAsList() ) {

                prescriberComments.add( PrescriberComment.build( ( ( JSONObject ) data ).toMap() ) );
            }

            return prescriberComments;
        }

        return new ArrayList<>();
    }
}
