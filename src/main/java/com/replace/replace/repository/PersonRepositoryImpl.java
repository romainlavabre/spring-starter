package com.replace.replace.repository;

import com.replace.replace.entity.Friend;
import com.replace.replace.entity.Person;
import com.replace.replace.exception.HttpNotFoundException;
import com.replace.replace.repository.jpa.PersonJpa;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service
public class PersonRepositoryImpl extends AbstractRepository< Person > implements PersonRepository {

    protected final PersonJpa personJpa;


    public PersonRepositoryImpl(
            EntityManager entityManager,
            PersonJpa personJpa ) {
        super( entityManager, personJpa );
        this.personJpa = personJpa;
    }


    @Override
    public Optional< Person > findByFriend( Friend friend ) {
        return personJpa.findByFriendsContains( friend );
    }


    @Override
    public Person findOrFailByFriend( Friend friend ) {
        Optional< Person > optionalPerson = findByFriend( friend );

        if ( optionalPerson.isPresent() ) {
            return optionalPerson.get();
        }

        throw new HttpNotFoundException( "PERSON_NOT_FOUND" );
    }


    @Override
    protected Class< Person > getClassType() {
        return Person.class;
    }
}
