package com.replace.replace.api.dynamic.api;

import com.replace.replace.configuration.dynamic.TriggerIdentifier;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface TriggerResolver< T > {
    TriggerIdentifier getId();


    Object getResource( T subject );
}
