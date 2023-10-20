package com.fairfair.callcenter.api.datasource;

import com.fairfair.callcenter.api.environment.Environment;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */

@Configuration
public class DataSourceConfiguration {

    protected final Environment environment;


    public DataSourceConfiguration( Environment environment ) {
        this.environment = environment;
    }


    @Bean
    public DataSource getDataSource() {
        DataSource readWrite = getReadWriteDataSource();
        DataSource readOnly  = getReadOnlyDataSource();

        return new DataSourceRouter(
                readWrite != null ? readWrite : readOnly,
                readOnly != null ? readOnly : readWrite
        );
    }


    private DataSource getReadWriteDataSource() {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl( environment.getEnv( "spring.datasource.url" ) );
            config.setUsername( environment.getEnv( "spring.datasource.username" ) );
            config.setPassword( environment.getEnv( "spring.datasource.password" ) );
            config.setReadOnly( false );
            config.setAutoCommit( false );
            return new HikariDataSource( config );
        } catch ( Throwable e ) {
            e.printStackTrace();
        }

        return null;
    }


    private DataSource getReadOnlyDataSource() {
        if ( environment.getEnv( "spring.datasource.read-only.url" ) == null
                || environment.getEnv( "spring.datasource.read-only.url" ).isBlank() ) {
            return null;
        }

        try {

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl( environment.getEnv( "spring.datasource.read-only.url" ) );
            config.setUsername( environment.getEnv( "spring.datasource.read-only.username" ) );
            config.setPassword( environment.getEnv( "spring.datasource.read-only.password" ) );
            config.setReadOnly( false );
            config.setAutoCommit( false );
            return new HikariDataSource( config );
        } catch ( Throwable e ) {
            e.printStackTrace();

            return null;
        }
    }
}
