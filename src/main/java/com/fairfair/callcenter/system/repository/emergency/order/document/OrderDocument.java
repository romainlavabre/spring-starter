package com.fairfair.callcenter.system.repository.emergency.order.document;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class OrderDocument {

    protected Long id;

    protected Long fileOrderId;

    protected Long order;


    protected OrderDocument() {
    }


    public Long getId() {
        return id;
    }


    public Long getFileOrderId() {
        return fileOrderId;
    }


    public Long getOrder() {
        return order;
    }


    protected static OrderDocument build( Map< String, Object > fields ) {
        OrderDocument orderDocument = new OrderDocument();

        orderDocument.id          = Cast.getLong( fields.get( "id" ) );
        orderDocument.fileOrderId = Cast.getLong( fields.get( "file_order_id" ) );
        orderDocument.order       = Cast.getLong( fields.get( "order_id" ) );

        return orderDocument;
    }
}
