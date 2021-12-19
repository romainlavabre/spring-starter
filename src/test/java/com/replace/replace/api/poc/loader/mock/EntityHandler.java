package com.replace.replace.api.poc.loader.mock;

import com.replace.replace.configuration.TopConfig;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class EntityHandler extends com.replace.replace.api.poc.kernel.entity.EntityHandler {

    @Override
    protected List< Class< ? > > getTypesAnnotated() {
        return TopConfig.getSubscribers();
    }
}
