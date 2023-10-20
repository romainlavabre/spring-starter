package com.fairfair.callcenter.system.repository.user.company.entrepreneur;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Entrepreneur {

    protected Long id;

    protected String authenticationId;

    protected String firstName;

    protected String lastName;

    protected String phone;

    protected String email;

    protected Long company;


    protected Entrepreneur() {
    }


    protected static Entrepreneur build( Map< String, Object > fields ) {
        Entrepreneur entrepreneur = new Entrepreneur();

        entrepreneur.id               = Cast.getLong( fields.get( "id" ) );
        entrepreneur.authenticationId = ( String ) fields.get( "authentication_id" );
        entrepreneur.firstName        = ( String ) fields.get( "first_name" );
        entrepreneur.lastName         = ( String ) fields.get( "last_name" );
        entrepreneur.phone            = ( String ) fields.get( "phone" );
        entrepreneur.email            = ( String ) fields.get( "email" );
        entrepreneur.company          = Cast.getLong( fields.get( "company_id" ) );


        return entrepreneur;
    }


    public Long getId() {
        return id;
    }


    public String getAuthenticationId() {
        return authenticationId;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getPhone() {
        return phone;
    }


    public String getEmail() {
        return email;
    }


    public Long getCompany() {
        return company;
    }
}
