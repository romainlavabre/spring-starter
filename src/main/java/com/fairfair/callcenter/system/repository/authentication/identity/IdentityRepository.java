package com.fairfair.callcenter.system.repository.authentication.identity;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface IdentityRepository {
    Optional< Identity > findById( long id );


    Identity findOrFail( long id );


    Optional< Identity > findByUsername( String username );


    Identity findOrFailByUsername( String username );
}
