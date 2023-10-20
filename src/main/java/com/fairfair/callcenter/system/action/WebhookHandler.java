package com.fairfair.callcenter.system.action;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface WebhookHandler {

    long getActionId();


    boolean isActionSuccess();


    short getNumberTry();


    String getHeader( String header );


    Object getResponsePayload( String parameter );


    String getResponsePayload();


    String getRelaunchLink();


    String getAbortLink();


    com.fairfair.callcenter.system.action.Action getOriginAction();
}
