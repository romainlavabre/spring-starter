package com.fairfair.callcenter.system.repository.user.company.address;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class CompanyAddress {
    protected Long id;

    protected String line1;

    protected String line2;

    protected String line3;

    protected String postcode;

    protected String city;


    protected CompanyAddress() {
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


    protected static CompanyAddress build( Map< String, Object > fields ) {
        CompanyAddress companyAddress = new CompanyAddress();

        companyAddress.id       = Cast.getLong( fields.get( "id" ) );
        companyAddress.line1    = ( String ) fields.get( "line_1" );
        companyAddress.line2    = ( String ) fields.get( "line_2" );
        companyAddress.line3    = ( String ) fields.get( "line_3" );
        companyAddress.postcode = ( String ) fields.get( "postcode" );
        companyAddress.city     = ( String ) fields.get( "city" );

        return companyAddress;
    }
}
