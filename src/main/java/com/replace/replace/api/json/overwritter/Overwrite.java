package com.replace.replace.api.json.overwritter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Overwrite< T > {

    Object overwrite( T data );
}
