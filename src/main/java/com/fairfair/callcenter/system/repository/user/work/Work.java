package com.fairfair.callcenter.system.repository.user.work;

import com.fairfair.callcenter.util.Cast;

import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class Work {
    protected Long id;

    protected String name;


    protected Work() {
    }


    protected static Work build( Map< String, Object > fields ) {
        Work work = new Work();

        work.id   = Cast.getLong( fields.get( "id" ) );
        work.name = ( String ) fields.get( "name" );

        return work;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }
}
