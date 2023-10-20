package com.fairfair.callcenter.system.repository.emergency.order.intervention.quality;

import com.fairfair.callcenter.util.Cast;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class QualityInspection {


    protected Long          id;
    protected Byte          currentStep;
    protected String        completedWorks;
    protected Boolean       deliveredWithoutReservation;
    protected String        deliveredComment;
    protected Boolean       quotationValidate;
    protected ZonedDateTime arrivedAt;
    protected ZonedDateTime leftAt;
    protected Boolean       presenceDetected;
    protected Long          nextOrder;
    protected Long          intervention;
    protected ZonedDateTime appointmentAt;
    protected ZonedDateTime travelAt;
    protected ZonedDateTime pictureBeforeAt;
    protected ZonedDateTime quotationAt;
    protected ZonedDateTime clientSignatureAt;
    protected ZonedDateTime workAt;
    protected ZonedDateTime pictureAfterAt;
    protected ZonedDateTime deliveryAt;


    protected QualityInspection() {
    }


    protected static QualityInspection build( Map< String, Object > fields ) {
        QualityInspection qualityInspection = new QualityInspection();

        qualityInspection.id                          = Cast.getLong( fields.get( "id" ) );
        qualityInspection.currentStep                 = Cast.getByte( fields.get( "current_step" ) );
        qualityInspection.completedWorks              = ( String ) fields.get( "completed_works" );
        qualityInspection.deliveredWithoutReservation = Cast.getBoolean( fields.get( "delivered_without_reservation" ) );
        qualityInspection.deliveredComment            = ( String ) fields.get( "delivered_comment" );
        qualityInspection.quotationValidate           = Cast.getBoolean( fields.get( "quotation_validate" ) );
        qualityInspection.arrivedAt                   = fields.get( "arrived_at" ) != null ? ZonedDateTime.parse( fields.get( "arrived_at" ).toString() ) : null;
        qualityInspection.leftAt                      = fields.get( "left_at" ) != null ? ZonedDateTime.parse( fields.get( "left_at" ).toString() ) : null;
        qualityInspection.presenceDetected            = Cast.getBoolean( fields.get( "presence_detected" ) );
        qualityInspection.nextOrder                   = Cast.getLong( fields.get( "next_order" ) );
        qualityInspection.intervention                = Cast.getLong( fields.get( "intervention_id" ) );
        qualityInspection.appointmentAt               = fields.get( "appointment_at" ) != null ? ZonedDateTime.parse( fields.get( "appointment_at" ).toString() ) : null;
        qualityInspection.travelAt                    = fields.get( "travel_at" ) != null ? ZonedDateTime.parse( fields.get( "travel_at" ).toString() ) : null;
        qualityInspection.pictureBeforeAt             = fields.get( "picture_before_at" ) != null ? ZonedDateTime.parse( fields.get( "picture_before_at" ).toString() ) : null;
        qualityInspection.quotationAt                 = fields.get( "quotation_at" ) != null ? ZonedDateTime.parse( fields.get( "quotation_at" ).toString() ) : null;
        qualityInspection.clientSignatureAt           = fields.get( "client_signature_at" ) != null ? ZonedDateTime.parse( fields.get( "client_signature_at" ).toString() ) : null;
        qualityInspection.workAt                      = fields.get( "work_at" ) != null ? ZonedDateTime.parse( fields.get( "work_at" ).toString() ) : null;
        qualityInspection.pictureAfterAt              = fields.get( "picture_after_at" ) != null ? ZonedDateTime.parse( fields.get( "picture_after_at" ).toString() ) : null;
        qualityInspection.deliveryAt                  = fields.get( "delivery_at" ) != null ? ZonedDateTime.parse( fields.get( "delivery_at" ).toString() ) : null;

        return qualityInspection;
    }


    public Long getId() {
        return id;
    }


    public Byte getCurrentStep() {
        return currentStep;
    }


    public String getCompletedWorks() {
        return completedWorks;
    }


    public Boolean getDeliveredWithoutReservation() {
        return deliveredWithoutReservation;
    }


    public String getDeliveredComment() {
        return deliveredComment;
    }


    public Boolean getQuotationValidate() {
        return quotationValidate;
    }


    public ZonedDateTime getArrivedAt() {
        return arrivedAt;
    }


    public ZonedDateTime getLeftAt() {
        return leftAt;
    }


    public Boolean getPresenceDetected() {
        return presenceDetected;
    }


    public Long getNextOrder() {
        return nextOrder;
    }


    public Long getIntervention() {
        return intervention;
    }


    public ZonedDateTime getAppointmentAt() {
        return appointmentAt;
    }


    public ZonedDateTime getTravelAt() {
        return travelAt;
    }


    public ZonedDateTime getPictureBeforeAt() {
        return pictureBeforeAt;
    }


    public ZonedDateTime getQuotationAt() {
        return quotationAt;
    }


    public ZonedDateTime getClientSignatureAt() {
        return clientSignatureAt;
    }


    public ZonedDateTime getWorkAt() {
        return workAt;
    }


    public ZonedDateTime getPictureAfterAt() {
        return pictureAfterAt;
    }


    public ZonedDateTime getDeliveryAt() {
        return deliveryAt;
    }
}
