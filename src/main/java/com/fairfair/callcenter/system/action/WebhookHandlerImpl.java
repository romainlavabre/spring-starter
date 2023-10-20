package com.fairfair.callcenter.system.action;

import com.fairfair.callcenter.api.request.Request;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class WebhookHandlerImpl implements WebhookHandler {

    private static final String           ACTION_ID            = "action_id";
    private static final String           ACTION_STATUS        = "action_status";
    private static final String           ACTION_NUMBER_TRY    = "action_number_call";
    private static final String           ACTION_RELAUNCH_LINK = "action_relaunch_link";
    private static final String           ACTION_ABORT_LINK    = "action_abort_link";
    private final        ActionRepository actionRepository;
    private              Request          request;


    public WebhookHandlerImpl(
            ActionRepository actionRepository ) {
        this.actionRepository = actionRepository;
    }


    @Override
    public long getActionId() {
        return Long.parseLong( String.valueOf( request.getParameter( ACTION_ID ) ) );
    }


    @Override
    public boolean isActionSuccess() {
        return ( int ) request.getParameter( ACTION_STATUS ) == 0;
    }


    @Override
    public short getNumberTry() {
        return ( short ) request.getParameter( ACTION_NUMBER_TRY );
    }


    @Override
    public String getHeader( String header ) {
        return ( String ) request.getParameter( "response_header_" + header );
    }


    @Override
    public Object getResponsePayload( String parameter ) {
        return request.getParameter( "response_payload_" + parameter );
    }


    @Override
    public String getResponsePayload() {
        Map< String, Object > response = request.getAllParameters( "response_payload_" );

        return response != null
                ? response.toString()
                : "";
    }


    @Override
    public Action getOriginAction() {
        Action action = actionRepository.findById( getActionId() ).get();
        action.setWebhookTreated( true )
              .setRemovable( true );

        return action;
    }


    @Override
    public String getRelaunchLink() {
        return request.getParameter( ACTION_RELAUNCH_LINK ).toString();
    }


    @Override
    public String getAbortLink() {
        return request.getParameter( ACTION_ABORT_LINK ).toString();
    }


    protected void setRequest( Request request ) {
        this.request = request;
    }
}
