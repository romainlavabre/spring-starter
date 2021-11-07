package com.replace.replace.repository.jpa;

import com.replace.replace.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Repository
public interface FriendJpa extends JpaRepository< Friend, Long > {

}
