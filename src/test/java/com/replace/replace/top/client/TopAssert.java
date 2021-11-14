package com.replace.replace.top.client;

import org.junit.jupiter.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TopAssert {

    private final ResponseEntity< Object > responseEntity;


    public TopAssert( ResponseEntity< Object > responseEntity ) {
        this.responseEntity = responseEntity;
    }


    public TopAssert is1xxCode() {
        Assertions.assertTrue( responseEntity.getStatusCodeValue() >= 100 && responseEntity.getStatusCodeValue() <= 199, String.valueOf( responseEntity.getStatusCodeValue() ) + ": " + (responseEntity.getBody() != null && responseEntity.getBody() instanceof Map ? (( Map< String, Object > ) responseEntity.getBody()).get( "message" ) : "NO MESSAGE") )
        ;

        return this;
    }


    public TopAssert is2xxCode() {
        Assertions.assertTrue( responseEntity.getStatusCodeValue() >= 200 && responseEntity.getStatusCodeValue() <= 299, String.valueOf( responseEntity.getStatusCodeValue() ) + ": " + (responseEntity.getBody() != null && responseEntity.getBody() instanceof Map ? (( Map< String, Object > ) responseEntity.getBody()).get( "message" ) : "NO MESSAGE") )
        ;

        return this;
    }


    public TopAssert is3xxCode() {
        Assertions.assertTrue( responseEntity.getStatusCodeValue() >= 300 && responseEntity.getStatusCodeValue() <= 399, String.valueOf( responseEntity.getStatusCodeValue() ) + ": " + (responseEntity.getBody() != null && responseEntity.getBody() instanceof Map ? (( Map< String, Object > ) responseEntity.getBody()).get( "message" ) : "NO MESSAGE") )
        ;

        return this;
    }


    public TopAssert is4xxCode() {
        Assertions.assertTrue( responseEntity.getStatusCodeValue() >= 400 && responseEntity.getStatusCodeValue() <= 499, String.valueOf( responseEntity.getStatusCodeValue() ) + ": " + (responseEntity.getBody() != null && responseEntity.getBody() instanceof Map ? (( Map< String, Object > ) responseEntity.getBody()).get( "message" ) : "NO MESSAGE") )
        ;

        return this;
    }


    public TopAssert is5xxCode() {
        Assertions.assertTrue( responseEntity.getStatusCodeValue() >= 500 && responseEntity.getStatusCodeValue() <= 599, String.valueOf( responseEntity.getStatusCodeValue() ) + ": " + (responseEntity.getBody() != null && responseEntity.getBody() instanceof Map ? (( Map< String, Object > ) responseEntity.getBody()).get( "message" ) : "NO MESSAGE") )
        ;

        return this;
    }


    public TopAssert isStatusCode( HttpStatus httpStatus ) {
        Assertions.assertEquals( httpStatus, responseEntity.getStatusCode() );

        return this;
    }


    public TopAssert containsHeader( String httpHeader ) {
        Assertions.assertTrue( responseEntity.getHeaders().containsKey( httpHeader ) && responseEntity.getHeaders().get( httpHeader ) != null );

        return this;
    }


    public TopAssert headerContainsValue( String httpHeader, String value ) {
        containsHeader( httpHeader );

        Assertions.assertEquals( responseEntity.getHeaders().get( httpHeader ).toString(), value );

        return this;
    }


    public Map< String, Object > getBodyAsMap() {
        return ( Map< String, Object > ) responseEntity.getBody();
    }


    public List< Object > getBodyAsList() {
        return ( List< Object > ) responseEntity.getBody();
    }
}
