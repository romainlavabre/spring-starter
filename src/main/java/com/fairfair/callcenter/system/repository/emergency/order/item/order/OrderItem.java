package com.fairfair.callcenter.system.repository.emergency.order.item.order;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class OrderItem {

    protected Long id;

    protected Double price;

    protected Short vat;

    protected Boolean enabled;

    protected Long order;

    protected Long defaultItem;


    protected OrderItem() {
    }


    public Long getId() {
        return id;
    }


    public Double getPrice() {
        return price;
    }


    public Short getVat() {
        return vat;
    }


    public Boolean isEnabled() {
        return enabled;
    }


    public Long getOrder() {
        return order;
    }


    public Long getDefaultItem() {
        return defaultItem;
    }


    protected static OrderItem build( Map< String, Object > fields ) {
        OrderItem orderItem = new OrderItem();

        orderItem.id          = Cast.getLong( fields.get( "id" ) );
        orderItem.price       = Cast.getDouble( fields.get( "price" ) );
        orderItem.vat         = Cast.getShort( fields.get( "vat" ) );
        orderItem.enabled     = ( Boolean ) fields.get( "enabled" );
        orderItem.order       = Cast.getLong( fields.get( "order_id" ) );
        orderItem.defaultItem = Cast.getLong( fields.get( "default_item_id" ) );

        return orderItem;
    }
}
