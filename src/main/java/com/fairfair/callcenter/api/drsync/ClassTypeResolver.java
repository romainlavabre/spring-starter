package com.fairfair.callcenter.api.drsync;

import jakarta.persistence.Entity;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ClassTypeResolver {
    private final Map< String, Class< ? > > DATA_REPOSITORY_SYNC = new HashMap<>();


    public ClassTypeResolver() {
        solveAll();
    }


    public Class< ? > get( String entityName ) {
        return DATA_REPOSITORY_SYNC.get( entityName );
    }


    public Map< String, Class< ? > > getAll() {
        return new HashMap<>( DATA_REPOSITORY_SYNC );
    }


    public boolean containsKey( String entityName ) {
        return DATA_REPOSITORY_SYNC.containsKey( entityName );
    }


    protected void solveAll() {
        String               searchPackage        = this.getClass().getPackageName().split( "\\.api" )[ 0 ];
        AnnotatedTypeScanner annotatedTypeScanner = new AnnotatedTypeScanner( Entity.class );
        Set< Class< ? > >    res                  = annotatedTypeScanner.findTypes( searchPackage );

        for ( Class< ? > clazz : res ) {
            DATA_REPOSITORY_SYNC.put( getDataRepositoryName( clazz ), clazz );
        }
    }


    protected String getDataRepositoryName( Class< ? > clazz ) {
        char[] letters = clazz.getSimpleName().toCharArray();

        StringBuilder entityName = new StringBuilder();

        for ( int i = 0; i < letters.length; i++ ) {
            String letter = String.valueOf( letters[ i ] );

            if ( i > 0 && letter.matches( "[A-Z]" ) ) {
                entityName.append( "_" )
                        .append( letter.toUpperCase() );
                continue;
            }

            entityName.append( letter.toUpperCase() );
        }

        return entityName.toString();
    }
}
