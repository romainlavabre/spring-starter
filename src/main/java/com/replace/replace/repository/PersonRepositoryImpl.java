package com.replace.replace.repository;

import com.replace.replace.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PersonRepositoryImpl extends AbstractRepository< Person > implements PersonRepository {

    public PersonRepositoryImpl(
            EntityManager entityManager,
            JpaRepository< Person, Long > jpaRepository ) {
        super( entityManager, jpaRepository );
    }


    @Override
    protected Class< Person > getClassType() {
        return Person.class;
    }
}
