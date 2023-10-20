package com.fairfair.callcenter.system.repository.user.admin;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface AdminRepository {

    Optional< Admin > findById( long id );


    Admin findOrFailById( long id );
}
