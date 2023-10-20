package com.fairfair.callcenter.system.repository.emergency.order.intervention.document.requested;

import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface RequestedDocumentRepository {

    Optional< RequestedDocument > findById( long id );


    RequestedDocument findOrFail( long id );


    List< RequestedDocument > findAllByInterventionId( long interventionId );
}
