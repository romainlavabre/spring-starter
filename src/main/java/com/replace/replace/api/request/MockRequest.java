package com.replace.replace.api.request;

import com.replace.replace.api.upload.UploadedFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class MockRequest implements Request {


    protected final Map< String, Object >       parameters;
    protected final Map< String, UploadedFile > files;


    public MockRequest() {
        this.parameters = new HashMap<>();
        this.files      = new HashMap<>();
    }


    @Override
    public Object getParameter( final String name ) {
        return this.parameters.get( name );
    }


    @Override
    public void setParameter( final String name, final Object value ) {
        this.parameters.put( name, value );
    }


    @Override
    public List< Object > getParameters( final String name ) {
        final List< Object > list = new ArrayList<>();

        this.parameters.forEach( ( key, value ) -> {
            list.add( value );
        } );

        return list;
    }


    @Override
    public String getQueryString( final String name ) {
        return null;
    }


    @Override
    public void setQueryString( final String name, final Object value ) {

    }


    @Override
    public String getClientIp() {
        return null;
    }


    @Override
    public UploadedFile getFile( final String name ) {
        return this.files.get( name );
    }


    @Override
    public List< UploadedFile > getFiles( final String name ) {
        final List< UploadedFile > list = new ArrayList<>();

        this.files.forEach( ( key, value ) -> {
            list.add( value );
        } );

        return list;
    }


    @Override
    public void setUploadedFile( final String name, final UploadedFile uploadedFile ) {
        this.files.put( name, uploadedFile );
    }


    @Override
    public String getHeader( final String name ) {
        return null;
    }


    @Override
    public String getContentType() {
        return "application/json";
    }


    @Override
    public Integer getPort() {
        return 8085;
    }


    @Override
    public String getHost() {
        return "localhost";
    }


    @Override
    public String getScheme() {
        return "http";
    }


    @Override
    public String getUri() {
        return "/api";
    }


    @Override
    public String getBaseUrl() {
        return "http://localhost:8085";
    }


    @Override
    public String getMethod() {
        return null;
    }


    public static Request build( final Map< String, Object > parameters, final Map< String, UploadedFile > files ) {

        final Request request = new MockRequest();

        parameters.forEach( request::setParameter );
        files.forEach( request::setUploadedFile );

        return request;
    }


    public static Request build( final Map< String, Object > parameters ) {

        final Request request = new MockRequest();

        parameters.forEach( request::setParameter );

        return request;
    }
}
