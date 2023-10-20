package com.fairfair.callcenter.system.repository.emergency.prescriber;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PrescriberSpecificityRepository {

    Optional< PrescriberSpecificity > findById( long id );


    PrescriberSpecificity findOrFail( long id );


    Optional< PrescriberSpecificity > findByPrescriberId( long id );


    PrescriberSpecificity findOrFailByPrescriberId( long id );
}
