package com.replace.replace.api.upload;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class UploadedFileImpl implements UploadedFile {

    private String name;
    private String contentType;
    private int    size;
    private byte[] content;
    private String path;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName( String name ) {
        this.name = name;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setContentType( String contentType ) {
        this.contentType = contentType;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public void setSize( int size ) {
        this.size = size;
    }

    @Override
    public byte[] getContent() {
        return this.content;
    }

    @Override
    public void setContent( byte[] content ) {
        this.content = content;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath( String path ) {
        this.path = path;
    }
}
