package com.fairfair.callcenter.system.repository.emergency.order.document;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface OrderDocumentRepository {
    Optional< OrderDocument > findById( long id );


    OrderDocument findOrFail( long id );


    Optional< OrderDocument > findByOrder( long id );


    OrderDocument findOrFailByOrder( long id );
}
