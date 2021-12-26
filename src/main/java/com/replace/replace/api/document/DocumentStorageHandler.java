package com.replace.replace.api.document;

import java.io.File;
import java.nio.ByteBuffer;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 * {@see https://github.com/romainlavabre/spring-starter-document.git}
 */
public interface DocumentStorageHandler {
    /**
     * Create a file on remote server
     *
     * @param path Destination ON remote server
     * @param file File
     * @return TRUE if file uploaded
     */
    boolean create( String path, File file );


    /**
     * Create a file on remote server
     *
     * @param path       Destination ON remote server
     * @param byteBuffer A buffer of byte of file
     * @return TRUE if file uploaded
     */
    boolean create( String path, ByteBuffer byteBuffer );


    /**
     * Create a file on remote server
     *
     * @param path  Destination ON remote server
     * @param bytes Array of bytes of file
     * @return TRUE is file uploaded
     */
    boolean create( String path, byte[] bytes );


    /**
     * Remove file on remote server
     *
     * @param path Location of file
     * @return TRUE if file removed
     */
    boolean remove( String path );


    /**
     * Move file to another path
     *
     * @param path    Location of file
     * @param newPath New location of file
     * @return TRUE if file was removed
     */
    boolean move( String path, String newPath );


    /**
     * @param path Location on server
     * @return Array of byte in server
     */
    byte[] getContent( String path );


    /**
     * Return a link for access to file
     *
     * @param path     Location of file in remote server
     * @param duration Time where link is available (on minutes)
     * @return The URL
     */
    String getUrl( String path, Integer duration );


    /**
     * Return a link for access to file
     *
     * @param path Location of file in remote server
     * @return The URL available for 20 minutes
     */
    String getUrl( String path );
}
