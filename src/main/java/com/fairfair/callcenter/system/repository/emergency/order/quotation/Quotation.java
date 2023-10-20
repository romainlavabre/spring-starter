package com.fairfair.callcenter.system.repository.emergency.order.quotation;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Quotation {

    protected Long id;

    protected Short vat;

    protected ZonedDateTime createdAt;

    protected Long intervention;

    protected List< Long > quotationItems;


    protected Quotation() {
    }


    public Long getId() {
        return id;
    }


    public Short getVat() {
        return vat;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Long getIntervention() {
        return intervention;
    }


    public List< Long > getQuotationItems() {
        return quotationItems;
    }


    protected static Quotation build( Map< String, Object > fields ) {
        Quotation quotation = new Quotation();

        quotation.id             = Cast.getLong( fields.get( "id" ) );
        quotation.vat            = Cast.getShort( fields.get( "vat" ) );
        quotation.createdAt      = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        quotation.intervention   = Cast.getLong( fields.get( "intervention_id" ) );
        quotation.quotationItems = ( List< Long > ) fields.get( "quotation_items_id" );

        return quotation;
    }
}
