package com.replace.replace.api.upload;

import com.replace.replace.api.event.EventSubscriber;
import com.replace.replace.api.upload.annotation.*;
import com.replace.replace.api.upload.exception.UploadException;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface UploadHandler extends EventSubscriber {

    @Move( moveRule = Object.class )
    @Duplication( duplicationRule = Object.class )
    @AcceptType( types = {
            ContentTypeResolver.APPLICATION_PDF,
            ContentTypeResolver.IMAGE_PNG,
            ContentTypeResolver.IMAGE_JPEG
    } )
    @Size( size = 1250000 )
    @TransactionSynchronized
    String DOCUMENT = "document";

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
