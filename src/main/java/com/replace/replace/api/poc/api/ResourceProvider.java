package com.replace.replace.api.poc.api;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface ResourceProvider {

    /**
     * @param subject Initial entity
     * @return Target resources
     */
    List< Object > getResources( Object subject );
}
