package com.replace.replace.kernel.product;

import com.replace.replace.api.crud.Update;
import com.replace.replace.api.event.Event;
import com.replace.replace.api.event.EventDispatcher;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import com.replace.replace.model.Product;
import com.replace.replace.parameter.ProductParameter;
import com.replace.replace.property.ProductProperty;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@Qualifier( "updateProductName" )
public class UpdateName implements Update< Product > {

    protected DataStorageHandler dataStorageHandler;
    protected HistoryHandler     historyHandler;
    protected EventDispatcher    eventDispatcher;

    public UpdateName(
            DataStorageHandler dataStorageHandler,
            HistoryHandler historyHandler,
            EventDispatcher eventDispatcher
    ) {
        this.dataStorageHandler = dataStorageHandler;
        this.historyHandler     = historyHandler;
        this.eventDispatcher    = eventDispatcher;
    }

    @Override
    public void update( Request request, Product product ) {
        String name = ( String ) request.getParameter( ProductParameter.NAME );

        product.setName( name );

        this.launchEvent( request, product );

        this.historyHandler.update( product, ProductProperty.NAME );
        this.dataStorageHandler.persist( product );
    }

    protected void launchEvent( Request request, Product product ) {

        this.eventDispatcher
                .newEvent( Event.PRODUCT_NAME_ALTERED, Map.of(
                        Request.class.getName(), request,
                        Product.class.getName(), product
                ) );
    }
}
