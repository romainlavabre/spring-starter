package com.fairfair.callcenter.system.repository.user.work;

import com.fairfair.callcenter.system.repository.user.company.Company;

import java.util.List;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface WorkRepository {
    long UNKNOW          = 0;
    long LOCKSMITH       = 1;
    long CARPENTRY       = 2;
    long ELECTRICITY     = 3;
    long GLAZING         = 4;
    long PLUMBING        = 6;
    long PAINT           = 13;
    long COVERAGE        = 15;
    long MASONRY         = 18;
    long BOILER          = 22;
    long AIR_CONDITIONER = 25;


    Optional< Work > findById( long id );


    Work findOrFail( long id );


    List< Work > findAllByCompany( Company company );
}
