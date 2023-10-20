package com.fairfair.callcenter.system.repository;

import com.fairfair.callcenter.api.rest.Response;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Result< T > {
    protected Optional< T > entity;
    protected Response      response;


    protected Result() {
    }


    public static < J > Result< J > build( Response response, Optional< J > optional ) {
        Result< J > result = new Result<>();

        result.response = response;
        result.entity   = optional;

        return result;
    }


    public boolean isEmpty() {
        return entity.isEmpty();
    }


    public boolean isPresent() {
        return entity.isPresent();
    }


    public T getEntity() {
        return entity.get();
    }


    public boolean isServerAvailable() {
        return response.status() != 0;
    }


    public boolean isSuccess() {
        return response.isSuccess();
    }


    public int getHttpCode() {
        return response.status();
    }
}
