package com.replace.replace.api.dynamic.api;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CustomProvider {

    /**
     * @param subject Initial entity
     * @return Target resources
     */
    List< Object > getResources( Object subject );
}
