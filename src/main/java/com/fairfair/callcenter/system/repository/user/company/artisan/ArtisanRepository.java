package com.fairfair.callcenter.system.repository.user.company.artisan;

import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ArtisanRepository {
    Optional< Artisan > findById( long id );


    Artisan findOrFail( long id );


    Optional< List< Artisan > > findAllByCompany( long id );


    List< Artisan > findOrFailAllByCompany( long id );
}
