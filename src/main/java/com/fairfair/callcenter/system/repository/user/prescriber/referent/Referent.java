package com.fairfair.callcenter.system.repository.user.prescriber.referent;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Referent {

    protected Long id;

    protected String firstName;

    protected String lastName;

    protected String email;

    protected String phone;

    protected String authenticationId;

    protected Long prescriber;


    protected Referent() {
    }


    protected static Referent build( Map< String, Object > fields ) {
        Referent referent = new Referent();

        referent.id               = Cast.getLong( fields.get( "id" ) );
        referent.authenticationId = ( String ) fields.get( "authentication_id" );
        referent.firstName        = ( String ) fields.get( "first_name" );
        referent.lastName         = ( String ) fields.get( "last_name" );
        referent.phone            = ( String ) fields.get( "phone" );
        referent.email            = ( String ) fields.get( "email" );
        referent.prescriber       = Cast.getLong( fields.get( "prescriber_id" ) );


        return referent;
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


    public String getEmail() {
        return email;
    }


    public String getPhone() {
        return phone;
    }


    public String getAuthenticationId() {
        return authenticationId;
    }


    public Long getPrescriber() {
        return prescriber;
    }
}
