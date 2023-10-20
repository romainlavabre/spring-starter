package com.fairfair.callcenter.system.repository.emergency.order.intervention;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Intervention {

    public static final byte STATUS_IN_PROGRESS    = 0;
    public static final byte STATUS_SUCCESS        = 1;
    public static final byte STATUS_FAILED         = 2;
    public static final byte STATUS_FORCED_SUCCESS = 3;

    protected Long id;

    protected String token;

    protected Double artisanPrice;

    protected Double clientPrice;

    protected Byte status;

    protected Long artisanId;

    protected Long companyId;

    protected Boolean livePayment;

    protected ZonedDateTime appointment;

    protected ZonedDateTime createdAt;

    protected Long order;

    protected Long suggestion;

    protected Long quotation;

    protected Long qualityInspection;

    protected Long survey;


    protected Intervention() {
    }


    protected static Intervention build( Map< String, Object > fields ) {
        Intervention intervention = new Intervention();

        intervention.id                = Cast.getLong( fields.get( "id" ) );
        intervention.token             = ( String ) fields.get( "token" );
        intervention.artisanPrice      = Cast.getDouble( fields.get( "artisan_price" ) );
        intervention.clientPrice       = Cast.getDouble( fields.get( "client_price" ) );
        intervention.artisanId         = Cast.getLong( fields.get( "artisan_id" ) );
        intervention.companyId         = Cast.getLong( fields.get( "company_id" ) );
        intervention.appointment       = fields.get( "appointment" ) != null ? ZonedDateTime.parse( ( String ) fields.get( "appointment" ) ) : null;
        intervention.status            = Cast.getByte( fields.get( "status" ) );
        intervention.livePayment       = Cast.getBoolean( fields.get( "live_payment" ) );
        intervention.createdAt         = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        intervention.order             = Cast.getLong( fields.get( "order_id" ) );
        intervention.suggestion        = Cast.getLong( fields.get( "suggestion_id" ) );
        intervention.quotation         = Cast.getLong( fields.get( "quotation_id" ) );
        intervention.qualityInspection = Cast.getLong( fields.get( "quality_inspection_id" ) );
        intervention.survey            = Cast.getLong( fields.get( "survey_id" ) );


        return intervention;
    }


    public Long getId() {
        return id;
    }


    public String getToken() {
        return token;
    }


    public Double getArtisanPrice() {
        return artisanPrice;
    }


    public Double getClientPrice() {
        return clientPrice;
    }


    public Byte getStatus() {
        return status;
    }


    public Long getArtisanId() {
        return artisanId;
    }


    public Long getCompanyId() {
        return companyId;
    }


    public Boolean isLivePayment() {
        return livePayment;
    }


    public ZonedDateTime getAppointment() {
        return appointment;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Long getOrder() {
        return order;
    }


    public Long getSuggestion() {
        return suggestion;
    }


    public Long getQuotation() {
        return quotation;
    }


    public Long getQualityInspection() {
        return qualityInspection;
    }


    public Long getSurvey() {
        return survey;
    }
}
