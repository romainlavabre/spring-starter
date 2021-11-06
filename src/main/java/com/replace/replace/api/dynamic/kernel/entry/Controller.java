package com.replace.replace.api.dynamic.kernel.entry;

import com.replace.replace.api.dynamic.kernel.router.RouteHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.api.storage.data.DataStorageHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class Controller {

    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;


    public Controller(
            DataStorageHandler dataStorageHandler,
            Request request ) {
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
    }


    public ResponseEntity< Map< String, Object > > getOne( @PathVariable( "id" ) long id ) {
        RouteHandler.getRoute( request );

        return null;
    }


    public ResponseEntity< List< Map< String, Object > > > getAll() {
        return null;
    }


    public ResponseEntity< Map< String, Object > > getOneBy( @PathVariable( "id" ) long id ) {
        return null;
    }


    public ResponseEntity< List< Map< String, Object > > > getAllBy( @PathVariable( "id" ) long id ) {
        return null;
    }


    @Transactional
    public ResponseEntity< Map< String, Object > > post() {
        return null;
    }


    @Transactional
    public ResponseEntity< Map< String, Object > > put( @PathVariable( "id" ) long id ) {
        return null;
    }


    @Transactional
    public ResponseEntity< Void > patch( @PathVariable( "id" ) long id ) {
        return null;
    }


    @Transactional
    public ResponseEntity< Void > delete( @PathVariable( "id" ) long id ) {
        return null;
    }
}
