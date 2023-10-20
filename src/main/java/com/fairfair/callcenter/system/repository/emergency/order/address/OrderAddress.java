package com.fairfair.callcenter.system.repository.emergency.order.address;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class OrderAddress {

    protected Long id;

    protected String line1;

    protected String line2;

    protected String line3;

    protected String postcode;

    protected String city;

    protected Double lat;

    protected Double lng;

    protected Long order;


    protected OrderAddress() {
    }


    public Long getId() {
        return id;
    }


    public String getLine1() {
        return line1;
    }


    public String getLine2() {
        return line2;
    }


    public String getLine3() {
        return line3;
    }


    public String getPostcode() {
        return postcode;
    }


    public String getCity() {
        return city;
    }


    public Double getLat() {
        return lat;
    }


    public Double getLng() {
        return lng;
    }


    public Long getOrder() {
        return order;
    }


    protected static OrderAddress build( Map< String, Object > fields ) {
        OrderAddress orderAddress = new OrderAddress();

        orderAddress.id       = Cast.getLong( fields.get( "id" ) );
        orderAddress.line1    = ( String ) fields.get( "line_1" );
        orderAddress.line2    = ( String ) fields.get( "line_2" );
        orderAddress.line3    = ( String ) fields.get( "line_3" );
        orderAddress.postcode = ( String ) fields.get( "postcode" );
        orderAddress.city     = ( String ) fields.get( "city" );
        orderAddress.lat      = Cast.getDouble( fields.get( "lat" ) );
        orderAddress.lng      = Cast.getDouble( fields.get( "lng" ) );
        orderAddress.order    = Cast.getLong( fields.get( "order_id" ) );

        return orderAddress;
    }
}
