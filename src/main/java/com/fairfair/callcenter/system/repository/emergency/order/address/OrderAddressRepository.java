package com.fairfair.callcenter.system.repository.emergency.order.address;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface OrderAddressRepository {
    Optional< OrderAddress > findById( long id );


    OrderAddress findOrFail( long id );
}
