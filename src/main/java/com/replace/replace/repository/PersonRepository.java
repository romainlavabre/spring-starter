package com.replace.replace.repository;

import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface PersonRepository extends DefaultRepository< Person > {
    Optional< Person > findByFriend( Friend friend );


    Person findOrFailByFriend( Friend friend );
}
