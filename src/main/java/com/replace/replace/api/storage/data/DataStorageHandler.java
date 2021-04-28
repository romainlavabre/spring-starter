package com.replace.replace.api.storage.data;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DataStorageHandler {

    /**
     * Follow this entity
     *
     * @param entity Target unit of persistence
     */
    void persist( Object entity );


    /**
     * @param entity Target unit of persistence
     */
    void remove( Object entity );


    /**
     * Commit in database
     */
    void save();
}
