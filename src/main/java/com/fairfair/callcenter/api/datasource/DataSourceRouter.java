package com.fairfair.callcenter.api.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class DataSourceRouter extends AbstractRoutingDataSource {

    public DataSourceRouter( DataSource writer, DataSource reader ) {
        Map< Object, Object > dataSources = new HashMap<>();
        dataSources.put( "writer", writer );
        dataSources.put( "reader", reader );

        setTargetDataSources( dataSources );
        setDefaultTargetDataSource( writer );
    }


    @Override
    protected Object determineCurrentLookupKey() {
        return ReadOnlyResolver.getInstance() != null && ReadOnlyResolver.getInstance().isReadOnly()
                ? "reader"
                : "writer";
    }
}
