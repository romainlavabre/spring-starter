package com.replace.replace.api.poc.annotation;

import com.replace.replace.api.crud.Delete;
import com.replace.replace.api.request.Request;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class DefaultDelete implements Delete< Object > {


    @Override
    public void delete( Request request, Object entity ) {

    }
}
