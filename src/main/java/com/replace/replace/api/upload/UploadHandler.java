package com.replace.replace.api.upload;

import com.replace.replace.api.event.EventSubscriber;
import com.replace.replace.api.upload.exception.UploadException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface UploadHandler extends EventSubscriber {

    /**
     * Upload a received file base on configuration
     *
     * @param uploadedFile The file uploaded
     * @param config       Configuration name
     * @return TRUE if file uploaded, FALSE else
     */
    boolean upload( UploadedFile uploadedFile, String config )
            throws UploadException;
}
