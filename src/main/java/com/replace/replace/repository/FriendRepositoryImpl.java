package com.replace.replace.repository;

import com.replace.replace.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class FriendRepositoryImpl extends AbstractRepository< Friend > implements FriendRepository {

    public FriendRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Friend, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Friend > getClassType() {
        return Friend.class;
    }
}
