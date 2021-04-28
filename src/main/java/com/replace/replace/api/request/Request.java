package com.replace.replace.api.request;

import com.replace.replace.api.upload.UploadedFile;

import java.util.List;

public interface Request {
    Object getParameter( String name );

    void setParameter( String name, Object value );

    List< Object > getParameters( String name );

    String getQueryString( String name );

    void setQueryString( String name, Object value );

    String getClientIp();

    UploadedFile getFile( String name );

    List< UploadedFile > getFiles( String name );

    void setUploadedFile( String name, UploadedFile uploadedFile );

    String getHeader( String name );

    String getContentType();

    Integer getPort();

    String getHost();

    String getScheme();

    String getUri();

    String getBaseUrl();

    String getMethod();
}
