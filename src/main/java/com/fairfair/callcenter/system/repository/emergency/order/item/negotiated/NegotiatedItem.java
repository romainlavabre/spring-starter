package com.fairfair.callcenter.system.repository.emergency.order.item.negotiated;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class NegotiatedItem {


    protected Long id;

    protected Double buyPrice;

    protected Double sellPrice;

    protected Long prescriberId;

    protected Long defaultItemId;

    protected ZonedDateTime createdAt;


    protected NegotiatedItem() {
    }


    protected static NegotiatedItem build( Map< String, Object > fields ) {
        NegotiatedItem negotiatedItem = new NegotiatedItem();

        negotiatedItem.id            = Cast.getLong( fields.get( "id" ) );
        negotiatedItem.prescriberId  = Cast.getLong( fields.get( "prescriber_id" ) );
        negotiatedItem.defaultItemId = Cast.getLong( fields.get( "default_item_id" ) );
        negotiatedItem.buyPrice      = Cast.getDouble( fields.get( "buy_price" ) );
        negotiatedItem.sellPrice     = Cast.getDouble( fields.get( "sell_price" ) );
        negotiatedItem.createdAt     = ZonedDateTime.parse( fields.get( "created_at" ).toString() );

        return negotiatedItem;
    }


    public Long getId() {
        return id;
    }


    public Double getBuyPrice() {
        return buyPrice;
    }


    public Double getSellPrice() {
        return sellPrice;
    }


    public Long getPrescriber() {
        return prescriberId;
    }


    public Long getDefaultItem() {
        return defaultItemId;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
