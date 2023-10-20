package com.fairfair.callcenter.system.repository.user.company.artisan.address;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ArtisanAddressRepository {
    Optional< ArtisanAddress > findById( long id );


    ArtisanAddress findOrFail( long id );
}
