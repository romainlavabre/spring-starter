package com.replace.replace.api.dynamic.kernel.entity;

import com.replace.replace.api.dynamic.annotation.DynamicEnabled;
import com.replace.replace.api.dynamic.kernel.router.Resolver;
import com.replace.replace.repository.DefaultRepository;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class EntityHandler {

    protected static final Map< Class< ? >, Entity > storage = new HashMap<>();


    public static Collection< Entity > toEntity() {
        if ( storage.size() > 0 ) {
            return storage.values();
        }

        String basePackage = Resolver.class.getPackage().getName().replace( ".api.dynamic.kernel.router", "" );

        Reflections reflections = new Reflections( basePackage );

        for ( Class< ? > managed : reflections.getTypesAnnotatedWith( DynamicEnabled.class ) ) {
            storage.put( managed, new Entity( managed ) );
        }

        return toEntity();
    }


    public static Entity getEntity( Class< ? > subject ) {
        for ( Map.Entry< Class< ? >, Entity > entry : storage.entrySet() ) {
            if ( entry.getKey().getName().equals( subject.getName() ) ) {
                return entry.getValue();
            }
        }

        return null;
    }


    public static class Entity {
        private final String suffixPlural;

        private final Class< ? extends DefaultRepository< ? > > repository;

        private final DynamicEnabled dynamicEnabled;

        private Class< ? > subject;


        public Entity( Class< ? > subject ) {
            dynamicEnabled    = subject.getAnnotation( DynamicEnabled.class );
            this.suffixPlural = dynamicEnabled.suffixPlural();
            this.repository   = dynamicEnabled.repository();
            this.subject      = subject;
        }


        public String getSuffixPlural() {
            return suffixPlural;
        }


        public Class< ? extends DefaultRepository< ? > > getRepository() {
            return repository;
        }


        public DynamicEnabled getDynamicEnabled() {
            return dynamicEnabled;
        }


        public Class< ? > getSubject() {
            return subject;
        }
    }
}
