package com.fairfair.callcenter;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.springframework.MariaDB4jSpringService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles( "test" )
class FairfairCallCenterApplicationTests {

    private static MariaDB4jSpringService DB;


    @BeforeAll
    public static void initDB() throws ManagedProcessException {
        DB = new MariaDB4jSpringService();
        DB.getConfiguration().addArg( "--user=root" );
        DB.setDefaultPort( 3305 );
        DB.start();
        DB.getDB().createDB( "service-call-center" );
    }


    @AfterAll
    public static void cleanup() {
        if ( DB != null ) DB.stop();
    }


    @Test
    void contextLoads() {
    }

}
