package com.replace.replace.api.pagination;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public abstract class AbstractDto {

    public AbstractDto() {
    }


    abstract protected void hydrate( Map< String, Object > data );
}
