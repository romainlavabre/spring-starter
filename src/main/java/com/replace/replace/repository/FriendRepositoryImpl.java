package com.replace.replace.repository;

import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;
import com.replace.replace.repository.jpa.FriendJpa;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class FriendRepositoryImpl extends AbstractRepository< Friend > implements FriendRepository {

    protected final FriendJpa friendJpa;


    public FriendRepositoryImpl(
            EntityManager entityManager,
            FriendJpa friendJpa ) {
        super( entityManager, friendJpa );
        this.friendJpa = friendJpa;
    }


    @Override
    public List< Friend > findByPerson( Person person ) {
        return friendJpa.findByPerson( person );
    }


    @Override
    protected Class< Friend > getClassType() {
        return Friend.class;
    }
}
