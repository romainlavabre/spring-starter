package com.replace.replace.api.json.overwritter;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface Overwrite< T > {

    T overwrite( T data );
}
