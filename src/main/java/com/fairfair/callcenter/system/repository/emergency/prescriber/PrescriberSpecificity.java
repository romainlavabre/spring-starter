package com.fairfair.callcenter.system.repository.emergency.prescriber;

import com.fairfair.callcenter.util.Cast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class PrescriberSpecificity {

    private long id;

    private Long prescriberId;

    private Long delayBeforeInternalAbort;

    private Boolean billCustomer;

    private Float nightSupplementStartHour;

    private Float nightSupplementEndHour;

    private Boolean notConformityWhenDuplication;

    private Boolean notConformityWhenNotQualified;

    private Integer numberCharacterForNotQualified;

    private String notSupportedDepartment;

    private Boolean acceptSmsToClient;

    private Boolean acceptMailToClient;

    private Boolean allowQuotationAutoValidation;

    private String allowQuotationAutoValidationRules;

    private Boolean allowOrderInvoiceIncrease;

    private List< Long > adminOddMission;

    private List< Long > adminPeerMission;

    private List< Byte > companyStatusToFollow;

    private List< Byte > companyStatusAssignable;


    public PrescriberSpecificity() {
    }


    protected static PrescriberSpecificity build( Map< String, Object > fields ) {
        PrescriberSpecificity prescriberSpecificity = new PrescriberSpecificity();

        prescriberSpecificity.id                                = Cast.getLong( fields.get( "id" ) );
        prescriberSpecificity.prescriberId                      = Cast.getLong( fields.get( "prescriber_id" ) );
        prescriberSpecificity.delayBeforeInternalAbort          = Cast.getLong( fields.get( "delay_before_internal_abort" ) );
        prescriberSpecificity.billCustomer                      = Cast.getBoolean( fields.get( "bill_customer" ) );
        prescriberSpecificity.nightSupplementStartHour          = Cast.getFloat( fields.get( "night_supplement_start_hour" ) );
        prescriberSpecificity.nightSupplementEndHour            = Cast.getFloat( fields.get( "night_supplement_end_hour" ) );
        prescriberSpecificity.notConformityWhenDuplication      = Cast.getBoolean( fields.get( "not_conformity_when_duplication" ) );
        prescriberSpecificity.notConformityWhenNotQualified     = Cast.getBoolean( fields.get( "not_conformity_when_not_qualified" ) );
        prescriberSpecificity.numberCharacterForNotQualified    = Cast.getInt( fields.get( "number_character_for_not_qualified" ) );
        prescriberSpecificity.notSupportedDepartment            = String.valueOf( fields.get( "not_supported_department" ) );
        prescriberSpecificity.acceptSmsToClient                 = Cast.getBoolean( fields.get( "accept_sms_to_client" ) );
        prescriberSpecificity.acceptMailToClient                = Cast.getBoolean( fields.get( "accept_mail_to_client" ) );
        prescriberSpecificity.allowQuotationAutoValidation      = Cast.getBoolean( fields.get( "allow_quotation_auto_validation" ) );
        prescriberSpecificity.allowQuotationAutoValidationRules = ( String ) fields.get( "allow_quotation_auto_validation_rules" );
        prescriberSpecificity.allowOrderInvoiceIncrease         = Cast.getBoolean( fields.get( "allow_order_invoice_increase" ) );
        prescriberSpecificity.adminOddMission                   = ( List< Long > ) fields.get( "admin_odd_mission" );
        prescriberSpecificity.adminPeerMission                  = ( List< Long > ) fields.get( "admin_peer_mission" );
        prescriberSpecificity.companyStatusToFollow             = ( List< Byte > ) fields.get( "company_status_to_follow" );
        prescriberSpecificity.companyStatusAssignable           = ( List< Byte > ) fields.get( "company_status_assignable" );

        return prescriberSpecificity;
    }


    public long getId() {
        return id;
    }


    public Long getPrescriberId() {
        return prescriberId;
    }


    public Long getDelayBeforeInternalAbort() {
        return delayBeforeInternalAbort;
    }


    public Boolean isBillCustomer() {
        return billCustomer;
    }


    public Float getNightSupplementStartHour() {
        return nightSupplementStartHour;
    }


    public Float getNightSupplementEndHour() {
        return nightSupplementEndHour;
    }


    public Boolean isNotConformityWhenDuplication() {
        return notConformityWhenDuplication;
    }


    public Boolean isNotConformityWhenNotQualified() {
        return notConformityWhenNotQualified;
    }


    public Integer getNumberCharacterForNotQualified() {
        return numberCharacterForNotQualified;
    }


    public String getNotSupportedDepartment() {
        return notSupportedDepartment;
    }


    public Boolean isAcceptSmsToClient() {
        return acceptSmsToClient;
    }


    public Boolean isAcceptMailToClient() {
        return acceptMailToClient;
    }


    public Boolean isAllowQuotationAutoValidation() {
        return allowQuotationAutoValidation;
    }


    public String getAllowQuotationAutoValidationRules() {
        return allowQuotationAutoValidationRules;
    }


    public Boolean isAllowOrderInvoiceIncrease() {
        return allowOrderInvoiceIncrease;
    }


    public List< Long > getAdminOddMission() {
        return adminOddMission;
    }


    public List< Long > getAdminPeerMission() {
        return adminPeerMission;
    }


    public List< Byte > getCompanyStatusToFollow() {
        if ( companyStatusToFollow == null ) {
            return null;
        }

        List< Byte > result = new ArrayList<>();

        for ( Object object : companyStatusToFollow ) {
            result.add( Cast.getByte( object ) );
        }

        return result;
    }


    public List< Byte > getCompanyStatusAssignable() {
        if ( companyStatusAssignable == null ) {
            return null;
        }

        List< Byte > result = new ArrayList<>();

        for ( Object object : companyStatusAssignable ) {
            result.add( Cast.getByte( object ) );
        }

        return result;
    }
}
