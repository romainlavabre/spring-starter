package com.replace.replace.module.person;

import com.replace.replace.entity.Person;
import com.replace.replace.repository.PersonRepository;
import com.replace.replace.top.client.PocClient;
import com.replace.replace.top.client.PocMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class PersonTest {

    @Test
    public void testGetPersonReturn200() {
        PocMock pocMock = PocClient.getMocker();

        PersonRepository personRepository = pocMock.getMock( PersonRepository.class );

        Mockito.when( personRepository.findOrFail( Mockito.anyLong() ) )
               .thenReturn( new Person() );

        PocClient.getClient()
                 .get( "/admin/persons/1" )
                 .execute()
                 .is2xxCode();
    }


    @Test
    public void testGetAllPersonReturn200() {
        PocMock pocMock = PocClient.getMocker();

        PersonRepository personRepository = pocMock.getMock( PersonRepository.class );

        Mockito.when( personRepository.findAll() )
               .thenReturn( List.of( new Person(), new Person() ) );

        List< Object > body =
                PocClient.getClient()
                         .get( "/admin/persons" )
                         .execute()
                         .is2xxCode()
                         .getBodyAsList();

        Assertions.assertEquals( 2, body.size() );
    }


    @Test
    public void testUpdatePersonName() {
        PocMock pocMock = PocClient.getMocker();
        Person  person  = new Person();

        PersonRepository personRepository = pocMock.getMock( PersonRepository.class );
        Mockito.when( personRepository.findOrFail( 1L ) )
               .thenReturn( person );

        PocClient.getClient()
                 .patch( "/admin/persons/1/name" )
                 .parameters( Map.of(
                         "person_name", "romain"
                 ) )
                 .execute()
                 .is2xxCode();

        Assertions.assertEquals( person.getName(), "romain" );
    }


    @Test
    public void testUpdatePersonIdReturn404() {
        PocClient.getClient()
                 .patch( "/admin/persons/1/id" )
                 .execute()
                 .is4xxCode()
                 .isStatusCode( HttpStatus.NOT_FOUND );
    }
}
