package com.fairfair.callcenter.system.repository.emergency.order.comment;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PrescriberCommentRepository {
    List< PrescriberComment > findAllByOrder( long orderId );
}
