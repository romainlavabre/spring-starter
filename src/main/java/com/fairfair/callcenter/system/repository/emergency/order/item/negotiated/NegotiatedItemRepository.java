package com.fairfair.callcenter.system.repository.emergency.order.item.negotiated;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface NegotiatedItemRepository {
    @Deprecated( forRemoval = true )
    long SHIFTING         = 11;
    @Deprecated( forRemoval = true )
    long MAIN_WORK        = 12;
    @Deprecated( forRemoval = true )
    long NIGHT_SUPPLEMENT = 50;


    Optional< NegotiatedItem > findByDefaultItemIdAndPrescriber( long defaultItemId, long prescriberId );


    NegotiatedItem findOrFailByDefaultItemIdAndPrescriber( long defaultItemId, long prescriberId );
}
