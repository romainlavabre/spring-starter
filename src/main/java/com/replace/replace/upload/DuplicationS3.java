package com.replace.replace.upload;

import com.replace.replace.api.upload.UploadedFile;
import com.replace.replace.api.upload.duplication.DuplicationRule;
import com.replace.replace.api.upload.exception.DuplicationException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class DuplicationS3 implements DuplicationRule {

    @Override
    public void exec( UploadedFile uploadedFile ) throws DuplicationException {

    }
}
