package com.replace.replace.repository.jpa;

import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface FriendJpa extends JpaRepository< Friend, Long > {

    List< Friend > findByPerson( Person person );
}
