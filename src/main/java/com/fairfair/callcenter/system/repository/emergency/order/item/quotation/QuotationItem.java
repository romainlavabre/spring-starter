package com.fairfair.callcenter.system.repository.emergency.order.item.quotation;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class QuotationItem {

    protected Long id;

    protected String name;

    protected String description;

    protected Double price;

    protected Double quantity;

    protected ZonedDateTime createdAt;

    protected Long defaultItem;

    protected Long quotation;


    protected QuotationItem() {
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }


    public Double getPrice() {
        return price;
    }


    public Double getQuantity() {
        return quantity;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Long getDefaultItem() {
        return defaultItem;
    }


    public Long getQuotation() {
        return quotation;
    }


    protected static QuotationItem build( Map< String, Object > fields ) {
        QuotationItem quotationItem = new QuotationItem();

        quotationItem.id          = Cast.getLong( fields.get( "id" ) );
        quotationItem.name        = ( String ) fields.get( "name" );
        quotationItem.description = ( String ) fields.get( "description" );
        quotationItem.price       = Cast.getDouble( fields.get( "price" ) );
        quotationItem.quantity    = Cast.getDouble( fields.get( "quantity" ) );
        quotationItem.createdAt   = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        quotationItem.defaultItem = Cast.getLong( fields.get( "default_item_id" ) );
        quotationItem.quotation   = Cast.getLong( fields.get( "quotation_id" ) );

        return quotationItem;
    }
}
