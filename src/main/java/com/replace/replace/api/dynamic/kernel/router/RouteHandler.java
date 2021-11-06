package com.replace.replace.api.dynamic.kernel.router;

import com.replace.replace.api.dynamic.annotation.*;
import com.replace.replace.api.dynamic.api.TriggerResolver;
import com.replace.replace.api.dynamic.kernel.exception.InvalidSetterParameterType;
import com.replace.replace.api.dynamic.kernel.exception.MultipleSetterFoundException;
import com.replace.replace.api.dynamic.kernel.exception.SetterNotFoundException;
import com.replace.replace.api.dynamic.kernel.exception.ToManySetterParameterException;
import com.replace.replace.api.dynamic.kernel.setter.SetterHandler;
import com.replace.replace.api.dynamic.kernel.util.Formatter;
import com.replace.replace.configuration.security.Role;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class RouteHandler {

    protected static final Map< String, List< Route > > storage = new HashMap<>();


    public static List< Route > toRoute( Class< ? > subject, Field field ) throws SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchFieldException, NoSuchMethodException {

        String id = subject.getName() + field.getName();

        if ( storage.containsKey( id ) ) {
            return storage.get( id );
        }

        EntryPoint entryPoint = field.getAnnotation( EntryPoint.class );

        if ( entryPoint == null ) {
            return new ArrayList<>();
        }

        List< Route > routes = new ArrayList<>();

        if ( entryPoint.getOne().enabled() ) {
            routes.addAll( getRoute( subject, entryPoint.getOne() ) );
        }

        if ( entryPoint.getAll().enabled() ) {
            routes.addAll( getRoute( subject, entryPoint.getAll() ) );
        }


        for ( GetOneBy getOneBy : entryPoint.getOneBy() ) {
            routes.addAll( getRoute( subject, getOneBy ) );
        }

        for ( GetAllBy getAllBy : entryPoint.getAllBy() ) {
            routes.addAll( getRoute( subject, getAllBy ) );
        }

        for ( Post post : entryPoint.post() ) {
            routes.addAll( getRoute( subject, post ) );
        }

        for ( Put put : entryPoint.put() ) {
            routes.addAll( getRoute( subject, put ) );
        }

        for ( Patch patch : entryPoint.patch() ) {
            routes.addAll( getRoute( subject, patch, field ) );
        }

        for ( Delete delete : entryPoint.delete() ) {
            routes.addAll( getRoute( subject, delete ) );
        }

        storage.put( id, routes );

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, GetOne getOne ) {
        List< Route > routes = new ArrayList<>();

        if ( getOne.roles().length == 1 && getOne.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( getOne, subject, role.getName() ) );
            }
        } else {
            for ( String role : getOne.roles() ) {
                routes.add( new Route( getOne, subject, role ) );
            }
        }

        if ( !getOne.authenticated() ) {
            routes.add( new Route( getOne, subject, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, GetAll getAll ) {
        List< Route > routes = new ArrayList<>();

        if ( getAll.roles().length == 1 && getAll.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( getAll, subject, role.getName() ) );
            }
        } else {
            for ( String role : getAll.roles() ) {
                routes.add( new Route( getAll, subject, role ) );
            }
        }

        if ( !getAll.authenticated() ) {
            routes.add( new Route( getAll, subject, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, GetOneBy getOneBy ) {
        List< Route > routes = new ArrayList<>();

        if ( getOneBy.roles().length == 1 && getOneBy.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( getOneBy, subject, role.getName() ) );
            }
        } else {
            for ( String role : getOneBy.roles() ) {
                routes.add( new Route( getOneBy, subject, role ) );
            }
        }

        if ( !getOneBy.authenticated() ) {
            routes.add( new Route( getOneBy, subject, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, GetAllBy getAllBy ) {
        List< Route > routes = new ArrayList<>();

        if ( getAllBy.roles().length == 1 && getAllBy.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( getAllBy, subject, role.getName() ) );
            }
        } else {
            for ( String role : getAllBy.roles() ) {
                routes.add( new Route( getAllBy, subject, role ) );
            }
        }

        if ( !getAllBy.authenticated() ) {
            routes.add( new Route( getAllBy, subject, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, Post post ) throws InvalidSetterParameterType, ToManySetterParameterException, MultipleSetterFoundException, SetterNotFoundException, NoSuchFieldException, NoSuchMethodException {
        List< Route > routes = new ArrayList<>();

        if ( post.roles().length == 1 && post.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( post, subject, role.getName() ) );
            }
        } else {
            for ( String role : post.roles() ) {
                routes.add( new Route( post, subject, role ) );
            }
        }

        if ( !post.authenticated() ) {
            routes.add( new Route( post, subject, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, Put put ) throws InvalidSetterParameterType, ToManySetterParameterException, MultipleSetterFoundException, SetterNotFoundException, NoSuchFieldException, NoSuchMethodException {
        List< Route > routes = new ArrayList<>();

        if ( put.roles().length == 1 && put.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( put, subject, role.getName() ) );
            }
        } else {
            for ( String role : put.roles() ) {
                routes.add( new Route( put, subject, role ) );
            }
        }

        if ( !put.authenticated() ) {
            routes.add( new Route( put, subject, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, Patch patch, Field field ) throws InvalidSetterParameterType, ToManySetterParameterException, MultipleSetterFoundException, SetterNotFoundException, NoSuchMethodException {
        List< Route > routes = new ArrayList<>();

        if ( patch.roles().length == 1 && patch.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( patch, subject, field, role.getName() ) );
            }
        } else {
            for ( String role : patch.roles() ) {
                routes.add( new Route( patch, subject, field, role ) );
            }
        }

        if ( !patch.authenticated() ) {
            routes.add( new Route( patch, subject, field, null ) );
        }

        return routes;
    }


    private static List< Route > getRoute( Class< ? > subject, Delete delete ) {
        List< Route > routes = new ArrayList<>();

        if ( delete.roles().length == 1 && delete.roles()[ 0 ].equals( "*" ) ) {
            for ( Field role : Role.class.getFields() ) {
                routes.add( new Route( delete, subject, role.getName() ) );
            }
        } else {
            for ( String role : delete.roles() ) {
                routes.add( new Route( delete, subject, role ) );
            }
        }

        if ( !delete.authenticated() ) {
            routes.add( new Route( delete, subject, null ) );
        }

        return routes;
    }


    public static class Route {
        private String path;

        private RequestMethod requestMethod;

        private String role;

        private Class< ? > subject;

        private List< SetterHandler.Setter > setters;

        private List< TriggerResolver< ? > > triggerResolvers;


        public Route(
                GetOne getOne,
                Class< ? > subject,
                String role ) {
            this.requestMethod = RequestMethod.GET;
            this.subject       = subject;
            this.role          = role;

            if ( !getOne.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + getOne.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) )
                    .add( "{id:[0-9]+}" );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                GetAll getAll,
                Class< ? > subject,
                String role ) {
            this.requestMethod = RequestMethod.GET;
            this.subject       = subject;
            this.role          = role;

            if ( !getAll.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + getAll.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                GetOneBy getOneBy,
                Class< ? > subject,
                String role ) {
            this.requestMethod = RequestMethod.GET;
            this.subject       = subject;
            this.role          = role;

            if ( !getOneBy.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + getOneBy.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) )
                    .add( "by" )
                    .add( Formatter.toSnakeCase( getOneBy.entity().getSimpleName() ) )
                    .add( "{id:[0-9]+}" );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                GetAllBy getAllBy,
                Class< ? > subject,
                String role ) {
            this.requestMethod = RequestMethod.GET;
            this.subject       = subject;
            this.role          = role;

            if ( !getAllBy.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + getAllBy.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) )
                    .add( "by" )
                    .add( Formatter.toSnakeCase( getAllBy.entity().getSimpleName() ) )
                    .add( "{id:[0-9]+}" );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                Post post,
                Class< ? > subject,
                String role ) throws NoSuchFieldException, SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchMethodException {
            this.requestMethod = RequestMethod.POST;
            this.subject       = subject;
            this.role          = role;
            setters            = new ArrayList<>();

            for ( String field : post.fields() ) {
                setters.add( SetterHandler.toSetter( subject.getDeclaredField( field ) ) );
            }

            if ( !post.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + post.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                Put put,
                Class< ? > subject,
                String role ) throws NoSuchFieldException, SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchMethodException {
            this.requestMethod = RequestMethod.PUT;
            this.subject       = subject;
            this.role          = role;
            setters            = new ArrayList<>();

            for ( String field : put.fields() ) {
                setters.add( SetterHandler.toSetter( subject.getDeclaredField( field ) ) );
            }

            if ( !put.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + put.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) )
                    .add( "{id:[0-9]+}" );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                Patch patch,
                Class< ? > subject,
                Field field,
                String role ) throws SetterNotFoundException, ToManySetterParameterException, MultipleSetterFoundException, InvalidSetterParameterType, NoSuchMethodException {
            this.requestMethod = RequestMethod.PATCH;
            this.subject       = subject;
            this.role          = role;
            setters            = new ArrayList<>();
            setters.add( SetterHandler.toSetter( field ) );

            if ( !patch.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + patch.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) )
                    .add( "{id:[0-9]+}" )
                    .add( Formatter.toSnakeCase( field.getName() ) );

            path = "/" + stringJoiner.toString();
        }


        public Route(
                Delete delete,
                Class< ? > subject,
                String role ) {
            this.requestMethod = RequestMethod.DELETE;
            this.subject       = subject;
            this.role          = role;

            if ( !delete.route().isBlank() ) {
                path = "/" + getPathPartRole( role ) + delete.route();
                return;
            }

            StringJoiner stringJoiner = new StringJoiner( "/" );
            stringJoiner
                    .add( getPathPartRole( role ) )
                    .add( Formatter.toSnakeCase( subject.getSimpleName() + subject.getAnnotation( DynamicEnabled.class ).suffixPlural() ) )
                    .add( "{id:[0-9]+}" );

            path = "/" + stringJoiner.toString();
        }


        private String getPathPartRole( String role ) {
            return role != null ? role.replace( "ROLE_", "" ).toLowerCase() : "guest";
        }


        public String getPath() {
            return path;
        }


        public RequestMethod getRequestMethod() {
            return requestMethod;
        }
    }
}
