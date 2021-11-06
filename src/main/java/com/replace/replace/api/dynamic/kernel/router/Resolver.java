package com.replace.replace.api.dynamic.kernel.router;

import com.replace.replace.api.dynamic.annotation.DynamicEnabled;
import com.replace.replace.api.dynamic.kernel.entry.Controller;
import com.replace.replace.api.dynamic.kernel.exception.InvalidSetterParameterType;
import com.replace.replace.api.dynamic.kernel.exception.MultipleSetterFoundException;
import com.replace.replace.api.dynamic.kernel.exception.SetterNotFoundException;
import com.replace.replace.api.dynamic.kernel.exception.ToManySetterParameterException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Resolver {
    protected final Logger                       logger = LoggerFactory.getLogger( this.getClass() );
    protected final RequestMappingHandlerMapping requestMappingHandlerMapping;
    protected final ApplicationContext           applicationContext;
    protected final Controller                   controller;


    public Resolver(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            ApplicationContext applicationContext,
            Controller controller ) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.applicationContext           = applicationContext;
        this.controller                   = controller;
    }


    @PostConstruct
    public void resolveRouter()
            throws NoSuchMethodException,
                   SetterNotFoundException,
                   ToManySetterParameterException,
                   MultipleSetterFoundException,
                   InvalidSetterParameterType,
                   NoSuchFieldException {

        Set< Class< ? > > manageds = scanClassWithAnnotation();

        for ( Class< ? > managed : manageds ) {
            logger.info( "Found " + managed + " for dynamic framework" );

            for ( Field field : managed.getDeclaredFields() ) {
                for ( RouteHandler.Route route : RouteHandler.toRoute( managed, field ) ) {

                    RequestMappingInfo requestMappingInfo = RequestMappingInfo
                            .paths( route.getPath() )
                            .methods( route.getRequestMethod() )
                            .build();

                    requestMappingHandlerMapping.registerMapping(
                            requestMappingInfo,
                            controller,
                            Controller.class.getMethod( "get" )
                    );

                    logger.info( route.getRequestMethod().toString() + " " + route.getPath() );
                }
            }
        }
    }


    private Set< Class< ? > > scanClassWithAnnotation() {
        String basePackage = Resolver.class.getPackage().getName().replace( ".api.dynamic.kernel.router", "" );

        Reflections reflections = new Reflections( basePackage );

        return reflections.getTypesAnnotatedWith( DynamicEnabled.class );
    }
}
