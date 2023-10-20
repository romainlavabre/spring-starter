package com.fairfair.callcenter.api.datasource;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
@RequestScope
public class ReadOnlyStorage {
    public boolean readOnly = false;
}
