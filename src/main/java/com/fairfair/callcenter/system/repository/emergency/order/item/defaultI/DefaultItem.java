package com.fairfair.callcenter.system.repository.emergency.order.item.defaultI;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class DefaultItem {

    protected Long id;

    protected String name;

    protected Double buyPrice;

    protected Double sellPrice;


    protected DefaultItem() {
    }


    protected static DefaultItem build( Map< String, Object > fields ) {
        DefaultItem defaultItem = new DefaultItem();

        defaultItem.id        = Cast.getLong( fields.get( "id" ) );
        defaultItem.name      = ( String ) fields.get( "name" );
        defaultItem.buyPrice  = Cast.getDouble( fields.get( "buy_price" ) );
        defaultItem.sellPrice = Cast.getDouble( fields.get( "sell_price" ) );

        return defaultItem;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public Double getBuyPrice() {
        return buyPrice;
    }


    public Double getSellPrice() {
        return sellPrice;
    }
}
