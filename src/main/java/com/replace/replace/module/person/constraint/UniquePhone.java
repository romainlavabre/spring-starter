package com.replace.replace.module.person.constraint;

import com.replace.replace.api.dynamic.api.CustomConstraint;
import com.replace.replace.exception.HttpConflictException;
import com.replace.replace.repository.PersonRepository;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "PersonUniquePhone" )
public class UniquePhone implements CustomConstraint {

    protected final PersonRepository personRepository;


    public UniquePhone( PersonRepository personRepository ) {
        this.personRepository = personRepository;
    }


    @Override
    public void check( Object entity, Object phone ) throws RuntimeException {
        if ( phone == null ) {
            return;
        }

        if ( personRepository.findByPhone( phone.toString() ).isPresent() ) {
            throw new HttpConflictException( "PERSON_PHONE_NOT_AVAILABLE" );
        }
    }
}
