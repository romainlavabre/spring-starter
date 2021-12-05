package com.replace.replace.api.poc.kernel.router;

import com.replace.replace.api.poc.annotation.*;
import com.replace.replace.api.poc.kernel.entity.EntityHandler;
import com.replace.replace.api.poc.kernel.exception.*;
import com.replace.replace.api.poc.kernel.setter.SetterHandler;
import com.replace.replace.api.poc.kernel.util.Formatter;
import com.replace.replace.api.request.Request;
import com.replace.replace.configuration.security.Role;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class RouteHandler {

    protected final Map< String, List< Route > > storage = new HashMap<>();

    protected final ApplicationContext applicationContext;
    protected final SetterHandler      setterHandler;
    protected final EntityHandler      entityHandler;


    public RouteHandler(
            ApplicationContext applicationContext,
            SetterHandler setterHandler,
            EntityHandler entityHandler ) {
        this.applicationContext = applicationContext;
        this.setterHandler      = setterHandler;
        this.entityHandler      = entityHandler;
    }


    public List< Route > toRoute( Class< ? > subject, Field field ) throws SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchFieldException, NoSuchMethodException {

        String id = com.replace.replace.api.poc.kernel.util.Formatter.toSnakeCase( subject.getSimpleName() ) + entityHandler.getEntity( field.getDeclaringClass() ).getSuffixPlural() + "::" + field.getName();

        if ( storage.containsKey( id ) ) {
            return storage.get( id );
        }

        EntryPoint entryPoint = field.getAnnotation( EntryPoint.class );

        List< Route > routes = new ArrayList<>();

        if ( entryPoint.getOne().enabled() ) {
            routes.addAll( getRoute( subject, entryPoint.getOne(), field ) );
        }

        if ( entryPoint.getAll().enabled() ) {
            routes.addAll( getRoute( subject, entryPoint.getAll(), field ) );
        }


        for ( GetOneBy getOneBy : entryPoint.getOneBy() ) {
            routes.addAll( getRoute( subject, getOneBy, field ) );
        }

        for ( GetAllBy getAllBy : entryPoint.getAllBy() ) {
            routes.addAll( getRoute( subject, getAllBy, field ) );
        }

        for ( Post post : entryPoint.post() ) {
            routes.addAll( getRoute( subject, post, field ) );
        }

        for ( Put put : entryPoint.put() ) {
            routes.addAll( getRoute( subject, put, field ) );
        }

        for ( Patch patch : entryPoint.patch() ) {
            routes.addAll( getRoute( subject, patch, field ) );
        }

        for ( Delete delete : entryPoint.delete() ) {
            routes.addAll( getRoute( subject, delete, field ) );
        }

        storage.put( id, routes );

        return routes;
    }


    public Route getRoute( Request request, Class< ? > httpType ) throws NoRouteMatchException {

        String pluralEntity = request.getUri().split( "/" )[ 2 ];

        for ( Map.Entry< String, List< Route > > entry : storage.entrySet() ) {
            String firstPart = entry.getKey().split( "::" )[ 0 ];

            if ( !pluralEntity.equals( firstPart ) ) {
                continue;
            }

            for ( Route route : entry.getValue() ) {
                if ( !route.isHttpType( httpType ) ) {
                    continue;
                }

                if ( route.isMatchWithPath( request.getUri() ) ) {
                    return route;
                }
            }
        }

        throw new NoRouteMatchException();
    }


    private List< Route > getRoute( Class< ? > subject, GetOne getOne, Field field ) throws NoSuchMethodException, SetterNotFoundException, NoSuchFieldException, ToManySetterParameterException, InvalidSetterParameterType, MultipleSetterFoundException {
        return getRouteCore( subject, getOne, field, getOne.roles(), getOne.authenticated(), getOne.route() );
    }


    private List< Route > getRoute( Class< ? > subject, GetAll getAll, Field field ) throws NoSuchMethodException, SetterNotFoundException, NoSuchFieldException, ToManySetterParameterException, InvalidSetterParameterType, MultipleSetterFoundException {
        return getRouteCore( subject, getAll, field, getAll.roles(), getAll.authenticated(), getAll.route() );
    }


    private List< Route > getRoute( Class< ? > subject, GetOneBy getOneBy, Field field ) throws NoSuchMethodException, SetterNotFoundException, NoSuchFieldException, ToManySetterParameterException, InvalidSetterParameterType, MultipleSetterFoundException {
        return getRouteCore( subject, getOneBy, field, getOneBy.roles(), getOneBy.authenticated(), getOneBy.route() );
    }


    private List< Route > getRoute( Class< ? > subject, GetAllBy getAllBy, Field field ) throws NoSuchMethodException, SetterNotFoundException, NoSuchFieldException, ToManySetterParameterException, InvalidSetterParameterType, MultipleSetterFoundException {
        return getRouteCore( subject, getAllBy, field, getAllBy.roles(), getAllBy.authenticated(), getAllBy.route() );
    }


    private List< Route > getRoute( Class< ? > subject, Post post, Field field ) throws InvalidSetterParameterType, ToManySetterParameterException, MultipleSetterFoundException, SetterNotFoundException, NoSuchFieldException, NoSuchMethodException {
        return getRouteCore( subject, post, field, post.roles(), post.authenticated(), post.route() );
    }


    private List< Route > getRoute( Class< ? > subject, Put put, Field field ) throws InvalidSetterParameterType, ToManySetterParameterException, MultipleSetterFoundException, SetterNotFoundException, NoSuchFieldException, NoSuchMethodException {
        return getRouteCore( subject, put, field, put.roles(), put.authenticated(), put.route() );
    }


    private List< Route > getRoute( Class< ? > subject, Patch patch, Field field ) throws InvalidSetterParameterType, ToManySetterParameterException, MultipleSetterFoundException, SetterNotFoundException, NoSuchMethodException, NoSuchFieldException {
        return getRouteCore( subject, patch, field, patch.roles(), patch.authenticated(), patch.route() );
    }


    private List< Route > getRoute( Class< ? > subject, Delete delete, Field field ) throws NoSuchMethodException, SetterNotFoundException, NoSuchFieldException, ToManySetterParameterException, InvalidSetterParameterType, MultipleSetterFoundException {
        return getRouteCore( subject, delete, field, delete.roles(), delete.authenticated(), delete.route() );
    }


    private List< Route > getRouteCore( Class< ? > subject, Object annotation, Field field, String[] roles, boolean authenticated, String route ) throws NoSuchMethodException, SetterNotFoundException, MultipleSetterFoundException, ToManySetterParameterException, InvalidSetterParameterType, NoSuchFieldException {
        List< Route > routes = new ArrayList<>();

        if ( roles.length == 1 && roles[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( annotation, subject, role.getName(), field, route ) );
            }
        } else {
            for ( String role : roles ) {
                routes.add( new Route( annotation, subject, role, field, route ) );
            }
        }

        if ( !authenticated ) {
            routes.add( new Route( annotation, subject, null, field, route ) );
        }

        return routes;
    }


    public class Route {
        private final String path;

        private final RequestMethod requestMethod;

        private final String role;

        private final Class< ? > subject;

        private List< SetterHandler.Setter > setters;

        private final List< Trigger > triggers;

        private final Object httpType;

        private Object executor;


        public Route(
                Object annotation,
                Class< ? > subject,
                String role,
                Field field,
                String route )
                throws NoSuchFieldException,
                       SetterNotFoundException,
                       ToManySetterParameterException,
                       MultipleSetterFoundException,
                       InvalidSetterParameterType,
                       NoSuchMethodException {
            this.requestMethod = getRequestMethod( annotation );
            this.subject       = subject;
            this.role          = role;
            this.httpType      = annotation;
            triggers           = new ArrayList<>();
            path               = getRoute( route, annotation, field );

            if ( annotation instanceof Post ) {
                setters = new ArrayList<>();

                for ( String localField : (( Post ) annotation).fields() ) {
                    setters.add( setterHandler.toSetter( subject.getDeclaredField( localField ) ) );
                }

                triggers.addAll( Arrays.asList( (( Post ) annotation).triggers() ) );

                if ( (( Post ) annotation).executor() != DefaultCreate.class ) {
                    executor = applicationContext.getBean( (( Post ) annotation).executor() );
                }
            } else if ( annotation instanceof Put ) {
                setters = new ArrayList<>();

                for ( String localField : (( Put ) annotation).fields() ) {
                    setters.add( setterHandler.toSetter( subject.getDeclaredField( localField ) ) );
                }

                triggers.addAll( Arrays.asList( (( Put ) annotation).triggers() ) );

                if ( (( Put ) annotation).executor() != DefaultUpdate.class ) {
                    executor = applicationContext.getBean( (( Put ) annotation).executor() );
                }
            } else if ( annotation instanceof Patch ) {
                setters = new ArrayList<>();
                setters.add( setterHandler.toSetter( field ) );

                triggers.addAll( Arrays.asList( (( Patch ) annotation).triggers() ) );

                if ( (( Patch ) annotation).executor() != DefaultUpdate.class ) {
                    executor = applicationContext.getBean( (( Patch ) annotation).executor() );
                }
            } else if ( annotation instanceof Delete ) {
                triggers.addAll( Arrays.asList( (( Delete ) annotation).triggers() ) );

                if ( (( Delete ) annotation).executor() != DefaultDelete.class ) {
                    executor = applicationContext.getBean( (( Delete ) annotation).executor() );
                }
            }
        }


        public String getPath() {
            return path;
        }


        public RequestMethod getRequestMethod() {
            return requestMethod;
        }


        public boolean isGetOne() {
            return httpType instanceof GetOne;
        }


        public boolean isGetAll() {
            return httpType instanceof GetAll;
        }


        public boolean isGetOneBy() {
            return httpType instanceof GetOneBy;
        }


        public boolean isGetAllBy() {
            return httpType instanceof GetAllBy;
        }


        public boolean isPost() {
            return httpType instanceof Post;
        }


        public boolean isPut() {
            return httpType instanceof Put;
        }


        public boolean isPatch() {
            return httpType instanceof Patch;
        }


        public boolean isDelete() {
            return httpType instanceof Delete;
        }


        public boolean isHttpType( Class< ? > httpType ) {
            return httpType.isAssignableFrom( this.httpType.getClass() );
        }


        public boolean isMatchWithPath( String uri ) {
            String[] uriPart  = uri.split( "/" );
            String[] pathPart = path.split( "/" );

            if ( uriPart.length != pathPart.length ) {
                return false;
            }

            StringJoiner uriCompare  = new StringJoiner( "/" );
            StringJoiner pathCompare = new StringJoiner( "/" );

            for ( int i = 0; i < uriPart.length; i++ ) {
                if ( pathPart[ i ].contains( "{" ) ) {
                    pathPart[ i ] = pathPart[ i ].replace( "{", "" ).replace( "}", "" );

                    if ( pathPart[ i ].contains( ":" ) ) {
                        String regex = pathPart[ i ].split( ":" )[ 1 ];

                        if ( uriPart[ i ].matches( regex ) ) {
                            pathPart[ i ] = uriPart[ i ];
                        }
                    } else {
                        pathPart[ i ] = uriPart[ i ];
                    }
                }

                uriCompare.add( uriPart[ i ] );
                pathCompare.add( pathPart[ i ] );
            }

            return uriCompare.toString().equals( pathCompare.toString() );
        }


        public Class< ? extends DefaultRepository< ? > > getRepository() {
            return entityHandler.getEntity( subject ).getRepository();
        }


        public String getRole() {
            return role;
        }


        public Object getHttpType() {
            return httpType;
        }


        public String getRepositoryMethod() {
            if ( httpType instanceof GetOneBy ) {
                if ( !(( GetOneBy ) httpType).method().isBlank() ) {
                    return (( GetOneBy ) httpType).method();
                }

                return "findOrFailBy" + com.replace.replace.api.poc.kernel.util.Formatter.toPascalCase( (( GetOneBy ) httpType).entity().getSimpleName() );
            }

            if ( httpType instanceof GetAllBy ) {
                if ( !(( GetAllBy ) httpType).method().isBlank() ) {
                    return (( GetAllBy ) httpType).method();
                }

                return "findAllBy" + com.replace.replace.api.poc.kernel.util.Formatter.toPascalCase( (( GetAllBy ) httpType).entity().getSimpleName() );
            }

            return null;
        }


        public Class< ? > getSubject() {
            return subject;
        }


        public List< SetterHandler.Setter > getSetters() {
            return setters;
        }


        public List< Trigger > getTriggers() {
            return triggers;
        }


        private String getRoute( String route, Object annotation, Field field ) {
            if ( !route.isBlank() ) {
                return "/" + getPathPartRole( role ) + "/" + getPluralEntity( subject ) + route;
            }

            String       idPart       = "{id:[0-9]+}";
            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( getPluralEntity( subject ) );

            if ( annotation instanceof GetOne
                    || annotation instanceof Put
                    || annotation instanceof Delete ) {
                stringJoiner.add( idPart );
            } else if ( annotation instanceof GetOneBy ) {
                stringJoiner
                        .add( "by" )
                        .add( com.replace.replace.api.poc.kernel.util.Formatter.toSnakeCase( (( GetOneBy ) annotation).entity().getSimpleName() ) )
                        .add( "{id:[0-9]+}" );
            } else if ( annotation instanceof GetAllBy ) {
                stringJoiner
                        .add( "by" )
                        .add( com.replace.replace.api.poc.kernel.util.Formatter.toSnakeCase( (( GetAllBy ) annotation).entity().getSimpleName() ) )
                        .add( idPart );
            } else if ( annotation instanceof Patch ) {
                stringJoiner
                        .add( idPart )
                        .add( com.replace.replace.api.poc.kernel.util.Formatter.toSnakeCase( field.getName() ) );
            }

            return "/" + stringJoiner.toString();
        }


        private RequestMethod getRequestMethod( Object annotation ) {
            if ( annotation instanceof GetOne
                    || annotation instanceof GetAll
                    || annotation instanceof GetOneBy
                    || annotation instanceof GetAllBy ) {
                return RequestMethod.GET;
            }

            if ( annotation instanceof Post ) {
                return RequestMethod.POST;
            }

            if ( annotation instanceof Put ) {
                return RequestMethod.PUT;
            }

            if ( annotation instanceof Patch ) {
                return RequestMethod.PATCH;
            }

            if ( annotation instanceof Delete ) {
                return RequestMethod.DELETE;
            }

            return null;
        }


        private String getPathPartRole( String role ) {
            return role != null ? role.replace( "ROLE_", "" ).toLowerCase() : "guest";
        }


        private String getPluralEntity( Class< ? > subject ) {
            return Formatter.toSnakeCase( subject.getSimpleName() + entityHandler.getEntity( subject ).getSuffixPlural() );
        }
    }
}
