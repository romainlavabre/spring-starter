package com.fairfair.callcenter.system.repository.emergency.order.item.quotation;

import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface QuotationItemRepository {
    Optional< QuotationItem > findById( long id );


    QuotationItem findOrFail( long id );


    Optional< List< QuotationItem > > findByQuotation( long id );
}
