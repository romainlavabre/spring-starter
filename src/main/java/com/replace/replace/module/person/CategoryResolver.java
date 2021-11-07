package com.replace.replace.module.person;

import com.replace.replace.api.dynamic.api.UnmanagedTrigger;
import com.replace.replace.api.request.Request;
import com.replace.replace.entity.Person;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class CategoryResolver implements UnmanagedTrigger {


    @Override
    public void handle( Request request, Object object ) {
        Person person = ( Person ) object;
        
        if ( person.getAge() <= 20 ) {
            person.setCategory( Person.CATEGORY_YOUNG );
        } else if ( person.getAge() <= 50 ) {
            person.setCategory( Person.CATEGORY_NORMAL );
        } else {
            person.setCategory( Person.CATEGORY_OLD );
        }
    }
}
