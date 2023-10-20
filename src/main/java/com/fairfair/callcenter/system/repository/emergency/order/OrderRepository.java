package com.fairfair.callcenter.system.repository.emergency.order;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface OrderRepository {
    Optional< Order > findById( long id );


    Order findOrFail( long id );


    Optional< Order > findByIntervention( long id );


    Order findOrFailByIntervention( long id );


    boolean isPrescriber( long orderId, long prescriberId );
}
