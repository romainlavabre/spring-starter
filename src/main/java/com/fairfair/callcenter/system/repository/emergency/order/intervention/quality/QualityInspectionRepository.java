package com.fairfair.callcenter.system.repository.emergency.order.intervention.quality;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface QualityInspectionRepository {
    Optional< QualityInspection > findById( long id );


    QualityInspection findOrFail( long id );
}
