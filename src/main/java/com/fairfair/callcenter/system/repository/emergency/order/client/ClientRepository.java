package com.fairfair.callcenter.system.repository.emergency.order.client;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ClientRepository {
    Optional< Client > findById( long id );


    Client findOrFail( long id );
}
