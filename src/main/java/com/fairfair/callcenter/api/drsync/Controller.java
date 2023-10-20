package com.fairfair.callcenter.api.drsync;

import com.fairfair.callcenter.api.datasource.ReadOnly;
import com.fairfair.callcenter.api.json.Encoder;
import com.fairfair.callcenter.configuration.json.GroupType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController( "SystemDataRepositoryControllerINTERNAL" )
@RequestMapping( path = "/system/data_repository" )
public class Controller {
    protected final ClassTypeResolver classTypeResolver;
    protected final EntityManager     entityManager;


    public Controller( ClassTypeResolver classTypeResolver, EntityManager entityManager ) {
        this.classTypeResolver = classTypeResolver;
        this.entityManager     = entityManager;
    }


    @GetMapping( "/available_types" )
    public ResponseEntity< Map< String, Class< ? > > > getAvailableTypes() {
        return ResponseEntity.ok( classTypeResolver.getAll() );
    }


    @ReadOnly
    @GetMapping( path = "/{before:[0-9-]+}/{entity}" )
    public ResponseEntity< List< Map< String, Object > > > getEntityBefore( @PathVariable( "before" ) long before, @PathVariable( "entity" ) String entity ) {
        if ( !classTypeResolver.containsKey( entity ) ) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        String computedQuery;

        if ( before < 0 ) {
            computedQuery = "SELECT * FROM `" + entity.toLowerCase() + "` ORDER BY id DESC LIMIT 200";
        } else {
            computedQuery = "SELECT * FROM `" + entity.toLowerCase() + "` WHERE id < " + before + " ORDER BY id DESC LIMIT 200";
        }

        Query query = entityManager.createNativeQuery( computedQuery, classTypeResolver.get( entity ) );

        return ResponseEntity.ok(
                Encoder.encode( query.getResultList(), GroupType.DATA_REPOSITORY )
        );
    }


    @ReadOnly
    @GetMapping( path = "/{entity}/{id:[0-9]+}" )
    public ResponseEntity< Map< String, Object > > getEntity( @PathVariable( "id" ) long id, @PathVariable( "entity" ) String entity ) {
        if ( !classTypeResolver.containsKey( entity ) ) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        String computedQuery = "SELECT * FROM `" + entity.toLowerCase() + "` WHERE id = " + id;


        Query query = entityManager.createNativeQuery( computedQuery, classTypeResolver.get( entity ) );

        return ResponseEntity.ok(
                Encoder.encode( query.getSingleResult(), GroupType.DATA_REPOSITORY )
        );
    }


    @ReadOnly
    @GetMapping( path = "/{entity}/updated_after/{after}" )
    public ResponseEntity< List< Map< String, Object > > > getEntityUpdatedAfter( @PathVariable( "after" ) ZonedDateTime after, @PathVariable( "entity" ) String entity ) {
        if ( !classTypeResolver.containsKey( entity ) ) {
            return ResponseEntity
                    .badRequest()
                    .build();
        }

        String computedQuery = "SELECT * FROM `" + entity.toLowerCase() + "` WHERE updated_at >= \"" + after.format( DateTimeFormatter.ofPattern( "yyyy-MM-dd HH:mm:ss" ) ) + "\"";

        Query query = entityManager.createNativeQuery( computedQuery, classTypeResolver.get( entity ) );

        return ResponseEntity.ok( Encoder.encode( query.getResultList(), GroupType.DATA_REPOSITORY ) );
    }
}
