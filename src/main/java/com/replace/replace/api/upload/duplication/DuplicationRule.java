package com.replace.replace.api.upload.duplication;

import com.replace.replace.api.request.UploadedFile;
import com.replace.replace.api.upload.exception.DuplicationException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface DuplicationRule {

    /**
     * You can rename this file or throw an DuplicationException
     *
     * @param uploadedFile
     */
    void exec( UploadedFile uploadedFile )
            throws DuplicationException;
}
