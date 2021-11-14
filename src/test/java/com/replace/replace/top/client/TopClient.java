package com.replace.replace.top.client;

import com.replace.replace.api.upload.UploadedFile;
import com.replace.replace.top.loader.Context;
import com.replace.replace.top.loader.mock.MockRequest;
import com.replace.replace.top.loader.mock.MockRequestMappingHandlerMapping;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TopClient {


    private String uri;

    private RequestMethod requestMethod;

    private Map< String, Object > parameters;

    private Map< String, UploadedFile > files;


    public TopClient uri( String uri ) {
        this.uri = uri;

        return this;
    }


    public TopClient get( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.GET;

        return this;
    }


    public TopClient post( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.POST;

        return this;
    }


    public TopClient patch( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.PATCH;

        return this;
    }


    public TopClient put( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.PUT;

        return this;
    }


    public TopClient delete( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.DELETE;

        return this;
    }


    public TopClient method( RequestMethod requestMethod ) {
        this.requestMethod = requestMethod;

        return this;
    }


    public TopClient parameters( Map< String, Object > payload ) {
        this.parameters = payload;

        return this;
    }


    public TopClient files( Map< String, UploadedFile > payload ) {
        this.files = payload;

        return this;
    }


    public TopAssert execute() {
        return execute( false );
    }


    public TopAssert execute( boolean debug ) {
        MockRequest mockRequest = Context.getMockRequest();

        mockRequest.setUri( uri );
        mockRequest.addValues( parameters, files );

        try {
            MockRequestMappingHandlerMapping.Route route          = Context.getRouteRepository().getRouteMatchWith( uri, requestMethod );
            ResponseEntity< Object >               responseEntity = ( ResponseEntity< Object > ) route.invoke( uri );

            return new TopAssert( responseEntity );
        } catch ( Throwable e ) {
            if ( debug ) {
                e.printStackTrace();
            }

            ResponseEntity< Object > responseEntity;
            Map< String, Object >    body = (new HashMap<>());
            body.put( "message", e.getMessage() );
            
            ResponseStatus responseStatus = e.getClass().getAnnotation( ResponseStatus.class );

            if ( responseStatus != null ) {
                responseEntity = ResponseEntity
                        .status( responseStatus.code() )
                        .body( body );
            } else {
                responseEntity = ResponseEntity
                        .status( HttpStatus.INTERNAL_SERVER_ERROR )
                        .body( body );
            }

            return new TopAssert( responseEntity );
        }
    }


    public static TopClient getClient() {
        return new TopClient();
    }


    public static TopMock getMocker() {
        List< Object > mocks = Context.getAllMocks();

        for ( Object object : mocks ) {
            Mockito.reset( object );
        }

        return new TopMock();
    }
}
