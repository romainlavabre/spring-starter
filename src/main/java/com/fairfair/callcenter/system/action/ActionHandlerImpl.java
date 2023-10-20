package com.fairfair.callcenter.system.action;

import com.fairfair.callcenter.api.request.Request;
import com.fairfair.callcenter.api.storage.data.DataStorageHandler;
import com.fairfair.callcenter.exception.HttpInternalServerErrorException;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class ActionHandlerImpl implements ActionHandler {

    protected final DataStorageHandler   dataStorageHandler;
    protected final WebhookHandler       webhookHandler;
    protected final OnTransactionSuccess onTransactionSuccess;


    public ActionHandlerImpl(
            DataStorageHandler dataStorageHandler,
            WebhookHandler webhookHandler,
            OnTransactionSuccess onTransactionSuccess ) {
        this.dataStorageHandler   = dataStorageHandler;
        this.webhookHandler       = webhookHandler;
        this.onTransactionSuccess = onTransactionSuccess;
    }


    @Override
    public void save( Action action ) {
        if ( action.getMaxRetry() == null && action.getTimeOut() == null ) {
            throw new HttpInternalServerErrorException( ErrorMessage.ACTION_INVALID_RETRY_LIMIT );
        }

        dataStorageHandler.persist( action );

        onTransactionSuccess.put( action );
    }


    @Override
    public WebhookHandler getWebhookHandler( Request request ) {
        WebhookHandlerImpl webhookHandlerImpl = ( WebhookHandlerImpl ) webhookHandler;
        webhookHandlerImpl.setRequest( request );

        return webhookHandler;
    }
}
