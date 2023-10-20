package com.fairfair.callcenter.system.repository.emergency.order.intervention.document;

import com.fairfair.callcenter.util.Cast;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class InterventionDocument {


    protected long         id;
    protected List< Long > pictureBeforesId;
    protected List< Long > pictureAftersId;
    protected Long         clientSignatureId;
    protected Long         artisanSignatureId;
    protected Long         intervention;


    protected InterventionDocument() {
    }


    protected static InterventionDocument build( Map< String, Object > fields ) {
        InterventionDocument interventionDocument = new InterventionDocument();

        interventionDocument.id                 = Cast.getLong( fields.get( "id" ) );
        interventionDocument.pictureBeforesId   = ( List< Long > ) fields.get( "picture_befores_id" );
        interventionDocument.pictureAftersId    = ( List< Long > ) fields.get( "picture_afters_id" );
        interventionDocument.clientSignatureId  = Cast.getLong( fields.get( "client_signature_id" ) );
        interventionDocument.artisanSignatureId = Cast.getLong( fields.get( "artisan_signature_id" ) );
        interventionDocument.intervention       = Cast.getLong( fields.get( "intervention_id" ) );

        return interventionDocument;
    }


    public Long getId() {
        return id;
    }


    public List< Long > getPictureBeforesId() {
        return pictureBeforesId;
    }


    public List< Long > getPictureAftersId() {
        return pictureAftersId;
    }


    public Long getClientSignatureId() {
        return clientSignatureId;
    }


    public Long getArtisanSignatureId() {
        return artisanSignatureId;
    }


    public Long getIntervention() {
        return intervention;
    }
}
