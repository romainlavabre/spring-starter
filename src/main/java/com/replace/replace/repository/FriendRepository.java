package com.replace.replace.repository;

import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;

import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public interface FriendRepository extends DefaultRepository< Friend > {

    List< Friend > findByPerson( Person person );
}
