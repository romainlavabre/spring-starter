package com.replace.replace.api.json;

import com.replace.replace.api.container.Container;
import com.replace.replace.api.json.entity.Friend;
import com.replace.replace.api.json.entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class EncoderTest {

    @BeforeEach
    public void initEncoder() {
        Container container = Mockito.mock( Container.class );

        new Encoder( container );
    }


    @Test
    public void test1() {
        Friend friend = new Friend();
        friend.setId( 2 )
              .setLevel( ( byte ) 3 )
              .setName( "Friend" );
        Person person = new Person();
        person.setId( 1 )
              .setName( "Person" )
              .setYearOfPerson( 20 )
              .addFriend( friend );
        friend.setPerson( person );


        Map< String, Object > result = Encoder.encode( person, "test1" );

        Assertions.assertTrue( result.containsKey( "id" ) );
        Assertions.assertEquals( 1L, result.get( "id" ) );
        Assertions.assertTrue( result.containsKey( "name" ) );
        Assertions.assertEquals( "Person", result.get( "name" ) );
        Assertions.assertTrue( result.containsKey( "year_of_person" ) );
        Assertions.assertEquals( 20d, result.get( "year_of_person" ) );
        Assertions.assertTrue( result.containsKey( "friends_id" ) );
        Assertions.assertEquals( List.of( 2L ), result.get( "friends_id" ) );
    }


    @Test
    public void test2() {
        Friend friend = new Friend();
        friend.setId( 2 )
              .setLevel( ( byte ) 3 )
              .setName( "Friend" );
        Person person = new Person();
        person.setId( 1 )
              .setName( "Person" )
              .setYearOfPerson( 20 )
              .addFriend( friend );
        friend.setPerson( person );


        Map< String, Object > result = Encoder.encode( person, "test2" );

        Assertions.assertTrue( result.containsKey( "friends" ) );
        Assertions.assertTrue( Collection.class.isAssignableFrom( result.get( "friends" ).getClass() ) );
        Assertions.assertTrue( Map.class.isAssignableFrom( (( Collection ) result.get( "friends" )).toArray()[ 0 ].getClass() ) );
    }
}
