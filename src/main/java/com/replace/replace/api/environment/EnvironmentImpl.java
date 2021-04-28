package com.replace.replace.api.environment;

import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class EnvironmentImpl implements Environment {

    protected org.springframework.core.env.Environment environment;

    public EnvironmentImpl( org.springframework.core.env.Environment environment ) {
        this.environment = environment;
    }

    @Override
    public String getEnv( String env ) {
        return this.environment.getProperty( env );
    }
}
