package com.fairfair.callcenter.system.repository.emergency.order.item.defaultI;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DefaultItemRepository {
    long EXTERNAL_END_INITIATOR = 2;
    long PACKAGE                = 10;
    long SHIFTING               = 11;
    long MAIN_WORK              = 12;
    long NIGHT_SUPPLEMENT       = 50;
    long COMPENSATION           = 54;
    long PROVISIONS             = 110;
    long COVER_REDESIGN         = 201;
    long TARPAULIN              = 202;
    long INTERVENTION_PACKAGE   = 203;


    Optional< DefaultItem > findById( long id );


    DefaultItem findOrFail( long id );
}
