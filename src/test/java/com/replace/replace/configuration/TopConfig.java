package com.replace.replace.configuration;

import com.replace.replace.entity.Car;
import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;

import java.util.Set;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class TopConfig {

    public static Set< Class< ? > > getSubscribers() {
        return Set.of(
                Car.class,
                Friend.class,
                Person.class
        );
    }
}
