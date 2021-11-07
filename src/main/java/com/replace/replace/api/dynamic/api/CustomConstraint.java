package com.replace.replace.api.dynamic.api;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface CustomConstraint {

    void check( Object entity, Object newValue )
            throws RuntimeException;
}
