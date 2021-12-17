package com.replace.replace.api.poc.loader.mock;

import com.replace.replace.api.poc.exception.HttpNotFoundException;
import com.replace.replace.api.poc.kernel.entry.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class MockRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final List< Route > routes = new ArrayList<>();


    @Override
    public void registerMapping( RequestMappingInfo mapping, Object handler, Method method ) {
        routes.add( new Route( mapping, ( Controller ) handler, method ) );
    }


    public Route getRouteMatchWith( String uri, RequestMethod requestMethod ) {
        for ( Route route : routes ) {
            if ( route.match( uri, requestMethod ) ) {
                return route;
            }
        }

        throw new HttpNotFoundException();
    }


    public class Route {
        protected final RequestMappingInfo requestMappingInfo;
        protected final Controller         controller;
        protected final Method             method;
        protected final RequestMethod      requestMethod;


        private Route(
                RequestMappingInfo requestMappingInfo,
                Controller controller,
                Method method ) {
            this.requestMappingInfo = requestMappingInfo;
            this.controller         = controller;
            this.method             = method;
            requestMethod           = ( RequestMethod ) requestMappingInfo.getMethodsCondition().getMethods().toArray()[ 0 ];
        }


        public boolean match( String uri, RequestMethod requestMethod ) {
            if ( this.requestMethod != requestMethod ) {
                return false;
            }

            String compareUri = getUri();

            if ( uriRequireParameter() ) {
                Long id = getId( uri );

                if ( id == null ) {
                    return false;
                }

                compareUri = compareUri.replace( "{id:[0-9]+}", id.toString() );
            }

            return compareUri.equals( uri );
        }


        public ResponseEntity< ? > invoke( String uri ) throws Throwable {
            try {
                if ( method.getName().equals( "getAll" ) || method.getName().equals( "post" ) ) {
                    return ( ResponseEntity< ? > ) method.invoke( controller );
                }

                return ( ResponseEntity< ? > ) method.invoke( controller, getId( uri ) );
            } catch ( IllegalAccessException | InvocationTargetException e ) {
                throw e.getCause();
            }
        }


        private Long getId( String activeUri ) {
            String uri = getUri();

            String[] activeUriSplit = activeUri.split( "/" );
            String[] uriSplit       = uri.split( "/" );

            if ( activeUriSplit.length != uriSplit.length ) {
                return null;
            }

            for ( int i = 0; i < uriSplit.length; i++ ) {

                if ( uriSplit[ i ].equals( "{id:[0-9]+}" ) ) {

                    if ( Pattern.matches( "[0-9]+", activeUriSplit[ i ] ) ) {
                        return Long.valueOf( activeUriSplit[ i ] );
                    }
                }
            }

            return null;
        }


        private boolean uriRequireParameter() {
            return getUri().contains( "{id:[0-9]+}" );
        }


        private String getUri() {
            return requestMappingInfo.getPatternValues().toArray()[ 0 ].toString();
        }
    }
}
