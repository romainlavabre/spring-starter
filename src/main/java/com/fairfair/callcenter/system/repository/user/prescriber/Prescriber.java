package com.fairfair.callcenter.system.repository.user.prescriber;

import com.fairfair.callcenter.util.Cast;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Prescriber {

    protected Long id;

    protected String name;

    protected String phone;

    protected String email;

    protected Long billingId;

    protected String emailReport;

    protected Boolean enabled;

    protected List< Long > referents;


    protected Prescriber() {
    }


    protected static Prescriber build( Map< String, Object > fields ) {
        Prescriber prescriber = new Prescriber();

        prescriber.id          = Cast.getLong( fields.get( "id" ) );
        prescriber.name        = ( String ) fields.get( "name" );
        prescriber.phone       = ( String ) fields.get( "phone" );
        prescriber.email       = ( String ) fields.get( "email" );
        prescriber.billingId   = Cast.getLong( fields.get( "billing_id" ) );
        prescriber.emailReport = ( String ) fields.get( "email_report" );
        prescriber.enabled     = ( Boolean ) fields.get( "enabled" );
        prescriber.referents   = ( List< Long > ) fields.get( "referents_id" );

        return prescriber;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getPhone() {
        return phone;
    }


    public String getEmail() {
        return email;
    }


    public Long getBillingId() {
        return billingId;
    }


    public String getEmailReport() {
        return emailReport;
    }


    public Boolean isEnabled() {
        return enabled;
    }


    public List< Long > getReferents() {
        return referents;
    }
}
