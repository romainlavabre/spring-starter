package com.replace.replace.api.poc.client;

import com.replace.replace.api.poc.loader.Context;
import com.replace.replace.api.poc.loader.mock.MockRequest;
import com.replace.replace.api.poc.loader.mock.MockRequestMappingHandlerMapping;
import com.replace.replace.api.request.UploadedFile;
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
public class PocClient {


    private String uri;

    private RequestMethod requestMethod;

    private Map< String, Object > parameters;

    private Map< String, UploadedFile > files;


    public PocClient uri( String uri ) {
        this.uri = uri;

        return this;
    }


    public PocClient get( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.GET;

        return this;
    }


    public PocClient post( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.POST;

        return this;
    }


    public PocClient patch( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.PATCH;

        return this;
    }


    public PocClient put( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.PUT;

        return this;
    }


    public PocClient delete( String uri ) {
        this.uri      = uri;
        requestMethod = RequestMethod.DELETE;

        return this;
    }


    public PocClient method( RequestMethod requestMethod ) {
        this.requestMethod = requestMethod;

        return this;
    }


    public PocClient parameters( Map< String, Object > payload ) {
        this.parameters = payload;

        return this;
    }


    public PocClient files( Map< String, UploadedFile > payload ) {
        this.files = payload;

        return this;
    }


    public PocAssert execute() {
        return execute( false );
    }


    public PocAssert execute( boolean debug ) {
        MockRequest mockRequest = Context.getMockRequest();

        mockRequest.setUri( uri );
        mockRequest.addValues( parameters, files );

        try {
            MockRequestMappingHandlerMapping.Route route          = Context.getRouteRepository().getRouteMatchWith( uri, requestMethod );
            ResponseEntity< Object >               responseEntity = ( ResponseEntity< Object > ) route.invoke( uri );

            return new PocAssert( responseEntity );
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

            return new PocAssert( responseEntity );
        }
    }


    public static PocClient getClient() {
        return new PocClient();
    }


    public static PocMock getMocker() {
        List< Object > mocks = Context.getAllMocks();

        for ( Object object : mocks ) {
            Mockito.reset( object );
        }

        return new PocMock();
    }
}
