package com.replace.replace.top.loader.mock;

import com.replace.replace.configuration.TopConfig;

import java.util.Set;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class EntityHandler extends com.replace.replace.api.top.kernel.entity.EntityHandler {

    protected Set< Class< ? > > getTypesAnnotated() {
        return TopConfig.getSubscribers();
    }
}
