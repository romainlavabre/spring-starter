package com.fairfair.callcenter.system.repository.user.company.artisan.address;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class ArtisanAddress {

    protected Long id;

    protected String line1;

    protected String line2;

    protected String line3;

    protected String postcode;

    protected String city;

    protected Long artisan;

    protected Double lat;

    protected Double lng;


    protected ArtisanAddress() {
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


    public Long getArtisan() {
        return artisan;
    }


    protected static ArtisanAddress build( Map< String, Object > fields ) {
        ArtisanAddress artisanAddress = new ArtisanAddress();

        artisanAddress.id       = Cast.getLong( fields.get( "id" ) );
        artisanAddress.line1    = ( String ) fields.get( "line_1" );
        artisanAddress.line2    = ( String ) fields.get( "line_2" );
        artisanAddress.line3    = ( String ) fields.get( "line_3" );
        artisanAddress.postcode = ( String ) fields.get( "postcode" );
        artisanAddress.city     = ( String ) fields.get( "city" );
        artisanAddress.lat      = Cast.getDouble( fields.get( "lat" ) );
        artisanAddress.lng      = Cast.getDouble( fields.get( "lng" ) );
        artisanAddress.artisan  = Cast.getLong( fields.get( "artisan_id" ) );

        return artisanAddress;
    }
}
