package com.fairfair.callcenter.system.repository.emergency.order;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Order {
    public static final byte  STATUS_TO_SUGGEST                       = 0;
    public static final byte  STATUS_SUGGESTED                        = 1;
    public static final byte  STATUS_WAIT_IN_PROGRESS                 = 2;
    public static final byte  STATUS_CLOSE                            = 3;
    public static final byte  STATUS_VALIDATE                         = 4;
    public static final byte  END_INITIATOR_INTERNAL                  = 1;
    public static final byte  END_INITIATOR_PRESCRIBER                = 2;
    public static final byte  END_INITIATOR_CLIENT                    = 3;
    public static final short END_REASON_NOT_CONFORMITY               = 1;
    public static final short END_REASON_KEY_RETRIEVED                = 2;
    public static final short END_REASON_CLIENT_NO_REPLY              = 3;
    public static final short END_REASON_NO_DETAILS                   = 4;
    public static final short END_REASON_DUPLICATES                   = 5;
    public static final short END_REASON_INSUFFICIENT_SUPPORTED_PRICE = 6;
    public static final short END_REASON_REFUSE_CLIENT_PRICE          = 7;
    public static final short END_REASON_REMOTE_QUOTATION             = 8;
    public static final short END_REASON_OUT_OF_ZONE                  = 9;

    protected Long id;

    protected Long prescriberId;

    protected Long adminId;

    protected Long workId;

    protected String externalId;

    protected String orderComment;

    protected String prescriberComment;

    protected Double maxPrice;

    protected Byte status;

    protected Byte endInitiator;

    protected Byte endReason;

    protected Boolean clientContacted;

    protected Boolean crcSent;

    protected String crcContent;

    protected Short lastTimeUnattributed;

    protected String insuranceName;

    protected String insuranceRank;

    protected ZonedDateTime createdAt;

    protected Long client;

    protected List< Long > orderItems;

    protected List< Long > interventions;

    protected List< Long > suggestions;

    protected Long document;

    protected Long orderAddress;


    protected Order() {
    }


    protected static Order build( Map< String, Object > fields ) {
        Order order = new Order();

        order.id                   = Cast.getLong( fields.get( "id" ) );
        order.prescriberId         = Cast.getLong( fields.get( "prescriber_id" ) );
        order.adminId              = Cast.getLong( fields.get( "admin_id" ) );
        order.workId               = Cast.getLong( fields.get( "work_id" ) );
        order.externalId           = ( String ) fields.get( "external_id" );
        order.orderComment         = ( String ) fields.get( "order_comment" );
        order.prescriberComment    = ( String ) fields.get( "prescriber_comment" );
        order.maxPrice             = Cast.getDouble( fields.get( "max_price" ) );
        order.status               = Cast.getByte( fields.get( "status" ) );
        order.endInitiator         = Cast.getByte( fields.get( "end_initiator" ) );
        order.endReason            = Cast.getByte( fields.get( "end_reason" ) );
        order.clientContacted      = ( Boolean ) fields.get( "client_contacted" );
        order.crcSent              = ( Boolean ) fields.get( "crc_sent" );
        order.crcContent           = ( String ) fields.get( "crc_content" );
        order.lastTimeUnattributed = Cast.getShort( fields.get( "last_time_unattributed" ) );
        order.insuranceName        = ( String ) fields.get( "insurance_name" );
        order.insuranceRank        = ( String ) fields.get( "insurance_rank" );
        order.createdAt            = ZonedDateTime.parse( ( String ) fields.get( "created_at" ) );
        order.client               = Cast.getLong( fields.get( "client_id" ) );
        order.orderItems           = ( List< Long > ) fields.get( "order_items_id" );
        order.interventions        = ( List< Long > ) fields.get( "interventions_id" );
        order.suggestions          = ( List< Long > ) fields.get( "suggestions_id" );
        order.document             = Cast.getLong( fields.get( "order_document_id" ) );
        order.orderAddress         = Cast.getLong( fields.get( "order_address_id" ) );

        return order;
    }


    public Long getId() {
        return id;
    }


    public Long getPrescriberId() {
        return prescriberId;
    }


    public Long getAdminId() {
        return adminId;
    }


    public Long getWorkId() {
        return workId;
    }


    public String getExternalId() {
        return externalId;
    }


    public String getOrderComment() {
        return orderComment;
    }


    public String getPrescriberComment() {
        return prescriberComment;
    }


    public Double getMaxPrice() {
        return maxPrice;
    }


    public Byte getStatus() {
        return status;
    }


    public Byte getEndInitiator() {
        return endInitiator;
    }


    public Byte getEndReason() {
        return endReason;
    }


    public Boolean isClientContacted() {
        return clientContacted;
    }


    public Boolean isCrcSent() {
        return crcSent;
    }


    public String getCrcContent() {
        return crcContent;
    }


    public Short getLastTimeUnattributed() {
        return lastTimeUnattributed;
    }


    public String getInsuranceName() {
        return insuranceName;
    }


    public String getInsuranceRank() {
        return insuranceRank;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public Long getClient() {
        return client;
    }


    public List< Long > getOrderItems() {
        return orderItems;
    }


    public List< Long > getInterventions() {
        return interventions;
    }


    public List< Long > getSuggestions() {
        return suggestions;
    }


    public Long getDocument() {
        return document;
    }


    public Long getOrderAddress() {
        return orderAddress;
    }
}
