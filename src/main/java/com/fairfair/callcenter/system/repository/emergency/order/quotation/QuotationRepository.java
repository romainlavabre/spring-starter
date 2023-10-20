package com.fairfair.callcenter.system.repository.emergency.order.quotation;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface QuotationRepository {
    Optional< Quotation > findById( long id );


    Quotation findOrFail( long id );


    Optional< Quotation > findByInterventionId( long id );


    Quotation findOrFailByInterventionId( long id );
}
