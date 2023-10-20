package com.fairfair.callcenter.system.event;

import com.fairfair.callcenter.api.json.Encoder;
import com.fairfair.callcenter.configuration.json.GroupType;
import com.fairfair.callcenter.system.action.Action;
import com.fairfair.callcenter.system.action.ActionHandler;
import com.fairfair.callcenter.system.action.RequestMethodType;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EventBuilderImpl implements EventBuilder {

    protected final ActionHandler actionHandler;


    public EventBuilderImpl( ActionHandler actionHandler ) {
        this.actionHandler = actionHandler;
    }


    @Override
    public void build( String eventName, Object entity, long timeout ) {
        Action action = new Action();

        action.setUrl( eventName )
              .setMethod( RequestMethodType.POST )
              .setTimeOut( timeout )
              .addRequestPayload( "entity", Encoder.encode( entity, GroupType.SYSTEM ) );

        actionHandler.save( action );
    }


    @Override
    public void build( String eventName, Object entity ) {
        build( eventName, entity, 60 * 60 );
    }
}
