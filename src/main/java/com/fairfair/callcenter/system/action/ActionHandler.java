package com.fairfair.callcenter.system.action;

import com.fairfair.callcenter.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ActionHandler {
    void save( Action action );


    WebhookHandler getWebhookHandler( Request request );
}
