package com.fairfair.callcenter.system.repository.emergency.order.client;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Client {

    protected Long id;

    protected String name;

    protected String phone1;

    protected String phone2;

    protected String email;

    protected Long order;


    protected Client() {
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }


    public String getPhone1() {
        return phone1;
    }


    public String getPhone2() {
        return phone2;
    }


    public String getEmail() {
        return email;
    }


    public Long getOrder() {
        return order;
    }


    protected static Client build( Map< String, Object > fields ) {
        Client client = new Client();

        client.id     = Cast.getLong( fields.get( "id" ) );
        client.name   = ( String ) fields.get( "name" );
        client.phone1 = ( String ) fields.get( "phone_1" );
        client.phone2 = ( String ) fields.get( "phone_2" );
        client.email  = ( String ) fields.get( "email" );
        client.order  = Cast.getLong( fields.get( "order_id" ) );

        return client;
    }
}
