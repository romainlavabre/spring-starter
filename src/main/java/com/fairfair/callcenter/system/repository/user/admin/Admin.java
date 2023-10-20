package com.fairfair.callcenter.system.repository.user.admin;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Admin {

    protected Long id;

    protected String firstName;

    protected String lastName;

    protected String mobilePhone;

    protected String secretaryPhone;

    protected String email;


    protected Admin() {
    }


    protected static Admin build( Map< String, Object > fields ) {
        Admin company = new Admin();

        company.id             = Cast.getLong( fields.get( "id" ) );
        company.firstName      = ( String ) fields.get( "first_name" );
        company.lastName       = ( String ) fields.get( "last_name" );
        company.mobilePhone    = ( String ) fields.get( "mobile_phone" );
        company.secretaryPhone = ( String ) fields.get( "secretary_phone" );
        company.email          = ( String ) fields.get( "email" );

        return company;
    }


    public Long getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getMobilePhone() {
        return mobilePhone;
    }


    public String getSecretaryPhone() {
        return secretaryPhone;
    }


    public String getEmail() {
        return email;
    }
}
