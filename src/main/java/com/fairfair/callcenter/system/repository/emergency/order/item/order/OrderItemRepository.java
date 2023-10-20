package com.fairfair.callcenter.system.repository.emergency.order.item.order;

import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface OrderItemRepository {
    Optional< OrderItem > findById( long id );


    OrderItem findOrFail( long id );


    Optional< List< OrderItem > > findByOrder( long id );
}
