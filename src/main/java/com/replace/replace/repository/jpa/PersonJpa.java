package com.replace.replace.repository.jpa;

import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface PersonJpa extends JpaRepository< Person, Long > {

    Optional< Person > findByFriendsContains( Friend friend );


    Optional< Person > findByPhone( String phone );
}
