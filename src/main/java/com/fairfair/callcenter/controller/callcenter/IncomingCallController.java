package com.fairfair.callcenter.controller.callcenter;

import com.fairfair.callcenter.api.request.Request;
import com.fairfair.callcenter.api.storage.data.DataStorageHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController( "CallCenterIncomingCallController" )
@RequestMapping( path = "/call_center/incoming_calls" )
public class IncomingCallController {

    protected final DataStorageHandler dataStorageHandler;
    protected final Request            request;


    public IncomingCallController( DataStorageHandler dataStorageHandler, Request request ) {
        this.dataStorageHandler = dataStorageHandler;
        this.request            = request;
    }


    @GetMapping
    public ResponseEntity< Map< String, Object > > solve() {
        System.out.println( request.getBody() );

        return ResponseEntity.noContent().build();
    }
}
