package com.fairfair.callcenter.system.repository.emergency.order.intervention;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface InterventionRepository {
    Optional< Intervention > findById( long id );


    Intervention findOrFail( long id );


    List< Intervention > findAllByOrder( long orderId );


    Optional< Intervention > findByOrderAndSuccess( long orderId );


    Optional< Intervention > findByOrderAndCurrent( long id );


    boolean isCompany( long interventionId, long companyId );


    Optional< Map< String, Object > > getCompanyDebt( long companyId );
}
