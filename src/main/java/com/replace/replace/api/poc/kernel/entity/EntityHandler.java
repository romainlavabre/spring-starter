package com.replace.replace.api.poc.kernel.entity;

import com.replace.replace.api.poc.annotation.PocEnabled;
import com.replace.replace.configuration.poc.Subject;
import com.replace.replace.repository.DefaultRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EntityHandler {

    protected Map< Class< ? >, Entity > storage;


    public Collection< Entity > toEntity( final ApplicationContext applicationContext ) {
        if ( this.storage != null ) {
            return this.storage.values();
        }

        this.storage = new HashMap<>();

        for ( final Class< ? > managed : this.getTypesAnnotated() ) {
            this.storage.put( managed, new Entity( managed, ( DefaultRepository< ? extends DefaultRepository< ? > > ) applicationContext.getBean( managed.getAnnotation( PocEnabled.class ).repository() ) ) );
        }

        return this.toEntity( applicationContext );
    }


    public Entity getEntity( final Class< ? > subject ) {
        for ( final Map.Entry< Class< ? >, Entity > entry : this.storage.entrySet() ) {
            if ( entry.getKey().getName().equals( subject.getName() ) ) {
                return entry.getValue();
            }
        }

        return null;
    }


    protected List< Class< ? > > getTypesAnnotated() {
        return Subject.getSubject();
    }


    public static class Entity {
        private final String suffixPlural;

        private final Class< ? extends DefaultRepository< ? > > repository;

        private final PocEnabled pocEnabled;

        private final Class< ? > subject;

        private final DefaultRepository< ? extends DefaultRepository< ? > > defaultRepository;


        public Entity( final Class< ? > subject, final DefaultRepository< ? extends DefaultRepository< ? > > defaultRepository ) {
            this.pocEnabled        = subject.getAnnotation( PocEnabled.class );
            this.suffixPlural      = this.pocEnabled.suffixPlural();
            this.repository        = this.pocEnabled.repository();
            this.defaultRepository = defaultRepository;
            this.subject           = subject;
        }


        public String getSuffixPlural() {
            return this.suffixPlural;
        }


        public Class< ? extends DefaultRepository< ? > > getRepository() {
            return this.repository;
        }


        public PocEnabled getDynamicEnabled() {
            return this.pocEnabled;
        }


        public Class< ? > getSubject() {
            return this.subject;
        }


        public DefaultRepository< ? extends DefaultRepository< ? > > getDefaultRepository() {
            return this.defaultRepository;
        }
    }
}
