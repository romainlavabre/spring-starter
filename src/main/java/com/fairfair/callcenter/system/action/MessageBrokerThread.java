package com.fairfair.callcenter.system.action;

import com.fairfair.callcenter.api.environment.Environment;
import com.fairfair.callcenter.api.json.Encoder;
import com.fairfair.callcenter.api.rest.RequestBuilder;
import com.fairfair.callcenter.api.rest.Rest;
import com.fairfair.callcenter.configuration.environment.Variable;
import com.fairfair.callcenter.system.repository.authentication.TokenProvider;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Scope( "prototype" )
public class MessageBrokerThread implements Runnable {

    protected final ActionRepository actionRepository;
    protected final Environment      environment;
    protected final TokenProvider    tokenProvider;
    protected final EntityManager    entityManager;
    protected       List< Long >     actionsId;


    public MessageBrokerThread( ActionRepository actionRepository, Environment environment, TokenProvider tokenProvider, EntityManager entityManager ) {
        this.actionRepository = actionRepository;
        this.environment      = environment;
        this.tokenProvider    = tokenProvider;
        this.entityManager    = entityManager;
        actionsId             = new ArrayList<>();
    }


    @Transactional
    @Override
    public void run() {
        List< Action > actions = actionRepository.findAllByIdIn( actionsId );

        actions.addAll( actionRepository.findAllByTreatedAndCreatedAtLessThan( false, ZonedDateTime.now( ZoneOffset.UTC ).minusSeconds( 20 ) ) );
        actions.removeIf( Action::isTreated );

        List< Map< String, Object > > encoded = Encoder.encode( actions );

        for ( Map< String, Object > encodedAction : encoded ) {
            encodedAction.put( "origin", environment.getEnv( Variable.APPLICATION_NAME ) );
        }

        Rest.builder()
            .init( RequestBuilder.POST, environment.getEnv( "service.message-broker.url" ) + "/system/actions" )
            .withBearerToken( tokenProvider.getToken() )
            .jsonBody( Map.of(
                    "action", encoded
            ) )
            .buildAndSend();

        for ( Action action : actions ) {
            action.setTreated( true );

            if ( action.getWebhook() == null ) {
                action.setRemovable( true );
            }

            entityManager.persist( action );
        }

        entityManager.flush();
    }


    public void setActions( List< Long > ids ) {
        this.actionsId = ids;
    }
}
