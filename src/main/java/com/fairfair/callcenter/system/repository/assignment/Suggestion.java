package com.fairfair.callcenter.system.repository.assignment;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Suggestion {
    public static final byte STATUS_PROPOSED = 0;
    public static final byte STATUS_ACCEPTED = 1;
    public static final byte STATUS_REFUSED  = 2;
    public static final byte STATUS_CLOSED   = 3;

    private Long id;

    private Byte status;

    private Long companyId;

    private String token;

    private String refusedReason;

    private ZonedDateTime createdAt;

    private ZonedDateTime acceptedAt;

    private ZonedDateTime refusedAt;

    private ZonedDateTime closedAt;

    private Long orderId;

    private Long b2cOrderId;


    protected Suggestion() {
    }


    public Long getId() {
        return id;
    }


    public Byte getStatus() {
        return status;
    }


    public Long getCompanyId() {
        return companyId;
    }


    public String getToken() {
        return token;
    }


    public String getRefusedReason() {
        return refusedReason;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public ZonedDateTime getAcceptedAt() {
        return acceptedAt;
    }


    public ZonedDateTime getRefusedAt() {
        return refusedAt;
    }


    public ZonedDateTime getClosedAt() {
        return closedAt;
    }


    public Long getOrderId() {
        return orderId;
    }

    public Long getB2cOrderId() {
        return b2cOrderId;
    }

    public static Suggestion build( Map< String, Object > fields ) {
        Suggestion suggestion = new Suggestion();

        suggestion.id = Cast.getLong( fields.get( "id" ) );
        suggestion.status = Cast.getByte( fields.get( "status" ) );
        suggestion.companyId = Cast.getLong( fields.get( "company_id" ) );
        suggestion.token = (String) fields.get( "token" );
        suggestion.refusedReason = (String) fields.get( "refused_reason" );
        suggestion.createdAt = fields.get( "created_at" ) != null ? ZonedDateTime.parse( fields.get( "created_at" ).toString() ) : null;
        suggestion.createdAt = fields.get( "accepted_at" ) != null ? ZonedDateTime.parse( fields.get( "accepted_at" ).toString() ) : null;
        suggestion.createdAt = fields.get( "refused_at" ) != null ? ZonedDateTime.parse( fields.get( "refused_at" ).toString() ) : null;
        suggestion.createdAt = fields.get( "closed_at" ) != null ? ZonedDateTime.parse( fields.get( "closed_at" ).toString() ) : null;
        suggestion.orderId = Cast.getLong( fields.get( "order_id" ) );
        suggestion.b2cOrderId = Cast.getLong( fields.get( "b2c_order_id" ) );

        return suggestion;
    }
}
