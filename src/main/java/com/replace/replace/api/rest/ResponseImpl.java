package com.replace.replace.api.rest;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class ResponseImpl implements Response {

    protected HttpResponse< JsonNode > response;


    @Override
    public int status() {
        return this.response.getStatus();
    }


    @Override
    public boolean isSuccess() {
        return this.response.getStatus() >= 200
                && this.response.getStatus() < 300;
    }


    @Override
    public boolean hasHeader( final String header ) {
        return this.response.getHeaders().containsKey( header );
    }


    @Override
    public String getHeader( final String header ) {
        return this.response.getHeaders().get( header ).get( 0 );
    }


    @Override
    public Map< String, Object > getBody() {
        return this.response.getBody().getObject().toMap();
    }


    protected Response supply( final HttpResponse< JsonNode > response ) {
        this.response = response;

        return this;
    }
}
