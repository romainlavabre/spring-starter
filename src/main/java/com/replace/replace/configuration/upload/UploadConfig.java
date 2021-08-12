package com.replace.replace.configuration.upload;

import com.replace.replace.api.upload.ContentTypeResolver;
import com.replace.replace.api.upload.annotation.*;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface UploadConfig {
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
}
