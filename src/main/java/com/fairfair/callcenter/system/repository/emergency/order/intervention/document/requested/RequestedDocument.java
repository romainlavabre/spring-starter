package com.fairfair.callcenter.system.repository.emergency.order.intervention.document.requested;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class RequestedDocument {

    public static final String CLIENT_PRICE_CERTIFICATE_NAME = "Attestation de RAC de {price}";
    public static final String CLIENT_PRICE_INVOICE_NAME     = "Facture de reste à charge sociétaire";
    public static final String DELIVERY_CERTIFICATE_NAME     = "Procès-Verbal de fin d'intervention";

    public static final byte STATUS_REQUESTED   = 0;
    public static final byte STATUS_PROVIDED    = 1;
    public static final byte STATUS_ACCEPTED    = 2;
    public static final byte STATUS_REFUSED     = 3;
    public static final byte STATUS_UNNECESSARY = 4;

    private long id;

    private String name;

    private String explanation;

    private Long documentId;

    private Byte status;

    private String refusedReason;

    private Long signatureId;

    private ZonedDateTime lastRelaunchAt;

    private ZonedDateTime createdAt;

    private Long intervention;


    protected static RequestedDocument build( Map< String, Object > fields ) {
        RequestedDocument requestedDocument = new RequestedDocument();

        requestedDocument.id             = Cast.getLong( fields.get( "id" ) );
        requestedDocument.name           = ( String ) fields.get( "name" );
        requestedDocument.explanation    = ( String ) fields.get( "explanation" );
        requestedDocument.documentId     = Cast.getLong( fields.get( "document_id" ) );
        requestedDocument.status         = Cast.getByte( fields.get( "status" ) );
        requestedDocument.refusedReason  = ( String ) fields.get( "refused_reason" );
        requestedDocument.signatureId    = Cast.getLong( fields.get( "signature_id" ) );
        requestedDocument.lastRelaunchAt = fields.get( "last_relaunch_at" ) != null ? ZonedDateTime.parse( fields.get( "last_relaunch_at" ).toString() ) : null;
        requestedDocument.createdAt      = ZonedDateTime.parse( fields.get( "created_at" ).toString() );
        requestedDocument.intervention   = Cast.getLong( fields.get( "intervention_id" ) );

        return requestedDocument;
    }


    public long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getExplanation() {
        return explanation;
    }


    public Byte getStatus() {
        return status;
    }


    public Long getSignatureId() {
        return signatureId;
    }


    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }


    public String getRefusedReason() {
        return refusedReason;
    }


    public Long getDocumentId() {
        return documentId;
    }


    public Long getIntervention() {
        return intervention;
    }


    public ZonedDateTime getLastRelaunchAt() {
        return lastRelaunchAt;
    }
}
