package com.fairfair.callcenter.system.repository.user.prescriber.referent;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ReferentRepository {
    Optional< Referent > findById( long id );


    Referent findOrFail( Long id );
}
