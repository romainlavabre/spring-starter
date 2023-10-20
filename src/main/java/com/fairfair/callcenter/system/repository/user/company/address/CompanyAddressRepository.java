package com.fairfair.callcenter.system.repository.user.company.address;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CompanyAddressRepository {
    Optional< CompanyAddress > findById( long id );


    CompanyAddress findOrFail( long id );
}
