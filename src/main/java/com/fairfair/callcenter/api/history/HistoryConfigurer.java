package com.fairfair.callcenter.api.history;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface HistoryConfigurer {

    Optional< Integer > getAuthorId();


    Optional< String > getAuthorName();


    Optional< String > getAuthorIp();
}
