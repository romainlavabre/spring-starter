package com.replace.replace.api.poc.loader.mock;

import com.replace.replace.configuration.TopConfig;

import java.util.Set;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class EntityHandler extends com.replace.replace.api.poc.kernel.entity.EntityHandler {

    protected Set< Class< ? > > getTypesAnnotated() {
        return TopConfig.getSubscribers();
    }
}
