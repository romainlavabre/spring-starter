package com.fairfair.callcenter.system.action;

import com.fairfair.callcenter.api.json.Encoder;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@RestController( "InternalActionLibraryController" )
@RequestMapping( "/system/actions" )
public class Controller {

    private final ActionRepository actionRepository;


    public Controller( ActionRepository actionRepository ) {
        this.actionRepository = actionRepository;
    }


    @Deprecated( since = "4.0.18" )
    @GetMapping( path = "/{after:[0-9]+}" )
    public ResponseEntity< List< Map< String, Object > > > getAllActionAfterId( @PathVariable( "after" ) long id ) {
        return getUntreatedAction();
    }


    @Transactional
    @GetMapping( path = "/untreated" )
    public ResponseEntity< List< Map< String, Object > > > getUntreatedAction() {

        List< Action > actions = actionRepository.findAllByTreated( false );


        for ( Action action : actions ) {
            action.setTreated( true );

            if ( action.getWebhook() == null ) {
                action.setRemovable( true );
            }

            actionRepository.save( action );
        }

        ZonedDateTime now = ZonedDateTime.now( ZoneId.of( "UTC" ) );

        if ( now.getSecond() >= 0 && now.getSecond() <= 2 ) {
            actionRepository.deleteByRemovableIsTrue();
            actionRepository.deleteByTreatedIsTrueAndRemovableIsFalseAndCreatedAtIsLessThan( ZonedDateTime.now( ZoneId.of( "UTC" ) ).minusHours( 24 ) );
        }

        return ResponseEntity
                .ok( Encoder.encode( actions ) );
    }
}
