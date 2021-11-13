package com.replace.replace.api.top.kernel.entity;

import com.replace.replace.api.top.annotation.DynamicEnabled;
import com.replace.replace.api.top.kernel.router.Resolver;
import com.replace.replace.repository.DefaultRepository;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EntityHandler {

    protected Map< Class< ? >, Entity > storage;


    public Collection< Entity > toEntity( ApplicationContext applicationContext ) {
        if ( storage != null ) {
            return storage.values();
        }

        storage = new HashMap<>();

        for ( Class< ? > managed : getTypesAnnotated() ) {
            System.out.println( managed );
            storage.put( managed, new Entity( managed, ( DefaultRepository< ? extends DefaultRepository< ? > > ) applicationContext.getBean( managed.getAnnotation( DynamicEnabled.class ).repository() ) ) );
        }

        return toEntity( applicationContext );
    }


    public Entity getEntity( Class< ? > subject ) {
        for ( Map.Entry< Class< ? >, Entity > entry : storage.entrySet() ) {
            if ( entry.getKey().getName().equals( subject.getName() ) ) {
                return entry.getValue();
            }
        }

        return null;
    }


    protected Set< Class< ? > > getTypesAnnotated() {
        String      basePackage = Resolver.class.getPackage().getName().replace( ".api.dynamic.kernel.router", "" );
        Reflections reflections = new Reflections( basePackage );

        return reflections.getTypesAnnotatedWith( DynamicEnabled.class );
    }


    public static class Entity {
        private final String suffixPlural;

        private final Class< ? extends DefaultRepository< ? > > repository;

        private final DynamicEnabled dynamicEnabled;

        private Class< ? > subject;

        private final DefaultRepository< ? extends DefaultRepository< ? > > defaultRepository;


        public Entity( Class< ? > subject, DefaultRepository< ? extends DefaultRepository< ? > > defaultRepository ) {
            dynamicEnabled         = subject.getAnnotation( DynamicEnabled.class );
            this.suffixPlural      = dynamicEnabled.suffixPlural();
            this.repository        = dynamicEnabled.repository();
            this.defaultRepository = defaultRepository;
            this.subject           = subject;
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


        public DefaultRepository< ? extends DefaultRepository< ? > > getDefaultRepository() {
            return defaultRepository;
        }
    }
}
