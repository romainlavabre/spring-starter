package com.fairfair.callcenter.system.repository.emergency.order.intervention.document;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface InterventionDocumentRepository {
    Optional< InterventionDocument > findById( long id );


    InterventionDocument findOrFail( long id );


    Optional< InterventionDocument > findByInterventionId( long id );


    InterventionDocument findOrFailByInterventionId( long id );
}
