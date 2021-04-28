package com.replace.replace.upload;

import com.replace.replace.api.upload.Move.MoveRule;
import com.replace.replace.api.upload.UploadedFile;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class MoveS3 implements MoveRule {

    @Override
    public void setDestination( UploadedFile uploadedFile ) {

    }

    @Override
    public boolean move( UploadedFile uploadedFile ) {
        return false;
    }
}
