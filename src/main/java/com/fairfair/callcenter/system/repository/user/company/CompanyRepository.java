package com.fairfair.callcenter.system.repository.user.company;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CompanyRepository {
    Optional< Company > findById( long id );


    Company findOrFail( long id );


    Optional< Company > findByEntrepreneurId( long id );


    Company findOrFailByEntrepreneur( long id );


    Company findOrFailByArtisan( long artisanId );


    Company findOrFailByBillingId( long companyId );


    Optional< Company > findByFairfairToolsCustomerId( long customerId );


    Company findOrFailByFairfairToolsCustomerId( long customerId );
}
