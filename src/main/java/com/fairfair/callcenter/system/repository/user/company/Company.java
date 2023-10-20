package com.fairfair.callcenter.system.repository.user.company;

import com.fairfair.callcenter.util.Cast;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Company {

    public static final byte STATUS_OK             = 0;
    public static final byte STATUS_GOOD           = 1;
    public static final byte STATUS_WARNING        = 2;
    public static final byte STATUS_BANNED         = 3;
    public static final byte STATUS_NOT_INTERESTED = 4;
    public static final byte STATUS_NETWORK_HEAD   = 5;

    protected Long id;

    protected String name;

    protected String email;

    protected String apeCode;

    protected String siret;

    protected LocalDate acceptedTermsAt;

    protected Long billingId;

    protected String phoneMobile;

    protected String phoneSecretary;

    protected Boolean workAllDay;

    protected Boolean workAllWeek;

    protected Byte status;

    protected Boolean subjectVat;

    protected Boolean prospect;

    protected Boolean depanethic;

    protected String comment;

    protected ZonedDateTime createdAt;

    protected List< Long > artisans;

    protected List< Long > works;

    protected Long entrepreneurId;

    protected Long companyAddressId;

    protected Boolean wantSuggestionCall;

    protected Boolean wantSuggestionMail;

    protected Boolean wantSuggestionSms;

    protected Long fairfairToolsCustomerId;


    protected Company() {
    }


    protected static Company build( Map< String, Object > fields ) {
        Company company = new Company();

        company.id                      = Cast.getLong( fields.get( "id" ) );
        company.name                    = ( String ) fields.get( "name" );
        company.email                   = ( String ) fields.get( "email" );
        company.apeCode                 = ( String ) fields.get( "ape_code" );
        company.siret                   = ( String ) fields.get( "siret" );
        company.acceptedTermsAt         = fields.get( "accepted_terms_at" ) == null ? null : LocalDate.parse( ( String ) fields.get( "accepted_terms_at" ) );
        company.billingId               = Cast.getLong( fields.get( "billing_id" ) );
        company.phoneMobile             = ( String ) fields.get( "phone_mobile" );
        company.phoneSecretary          = ( String ) fields.get( "phone_secretary" );
        company.workAllDay              = ( Boolean ) fields.get( "work_all_day" );
        company.workAllWeek             = ( Boolean ) fields.get( "work_all_week" );
        company.status                  = Cast.getByte( fields.get( "status" ) );
        company.subjectVat              = ( Boolean ) fields.get( "subject_vat" );
        company.prospect                = ( Boolean ) fields.get( "prospect" );
        company.depanethic              = ( Boolean ) fields.get( "depanethic" );
        company.comment                 = ( String ) fields.get( "comment" );
        company.createdAt               = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        company.artisans                = ( List< Long > ) fields.get( "artisans_id" );
        company.works                   = ( List< Long > ) fields.get( "works_id" );
        company.entrepreneurId          = Cast.getLong( fields.get( "entrepreneur_id" ) );
        company.companyAddressId        = Cast.getLong( fields.get( "company_address_id" ) );
        company.wantSuggestionCall      = ( Boolean ) fields.get( "want_suggestion_call" );
        company.wantSuggestionMail      = ( Boolean ) fields.get( "want_suggestion_mail" );
        company.wantSuggestionSms       = ( Boolean ) fields.get( "want_suggestion_sms" );
        company.fairfairToolsCustomerId = Cast.getLong( fields.get( "fairfair_tools_customer_id" ) );


        return company;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getApeCode() {
        return apeCode;
    }


    public String getSiret() {
        return siret;
    }


    public LocalDate getAcceptedTermsAt() {
        return acceptedTermsAt;
    }


    public Long getBillingId() {
        return billingId;
    }


    public String getPhoneMobile() {
        return phoneMobile;
    }


    public String getPhoneSecretary() {
        return phoneSecretary;
    }


    public Boolean isWorkAllDay() {
        return workAllDay;
    }


    public Boolean isWorkAllWeek() {
        return workAllWeek;
    }


    public Byte getStatus() {
        return status;
    }


    public Boolean isSubjectVat() {
        return subjectVat;
    }


    public Boolean isProspect() {
        return prospect;
    }


    public Boolean isDepanethic() {
        return depanethic;
    }


    public String getComment() {
        return comment;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public List< Long > getArtisans() {
        return artisans;
    }


    public List< Long > getWorks() {
        return works;
    }


    public Long getEntrepreneurId() {
        return entrepreneurId;
    }


    public Long getCompanyAddressId() {
        return companyAddressId;
    }


    public Boolean isWantSuggestionCall() {
        return wantSuggestionCall != null
                ? wantSuggestionCall
                : false;
    }


    public Boolean isWantSuggestionMail() {
        return wantSuggestionMail;
    }


    public Boolean isWantSuggestionSms() {
        return wantSuggestionSms;
    }


    public Long getFairfairToolsCustomerId() {
        return fairfairToolsCustomerId;
    }
}
