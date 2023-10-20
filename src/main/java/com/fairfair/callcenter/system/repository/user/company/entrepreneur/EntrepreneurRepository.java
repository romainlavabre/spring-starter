package com.fairfair.callcenter.system.repository.user.company.entrepreneur;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface EntrepreneurRepository {
    Optional< Entrepreneur > findById( long id );


    Optional< Entrepreneur > findByEmail( String email );


    Entrepreneur findOrFail( Long id );


    Optional< Entrepreneur > findByAuthenticationId( long id );


    Entrepreneur findOrFailByAuthenticationId( long id );
}
