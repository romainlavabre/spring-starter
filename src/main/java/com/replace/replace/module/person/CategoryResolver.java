package com.replace.replace.module.person;

import com.replace.replace.api.crud.Update;
import com.replace.replace.api.request.Request;
import com.replace.replace.entity.Person;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class CategoryResolver implements Update< Person > {

    @Override
    public void update( Request request, Person person ) {
        if ( person.getAge() <= 20 ) {
            person.setCategory( Person.CATEGORY_YOUNG );
        } else if ( person.getAge() <= 50 ) {
            person.setCategory( Person.CATEGORY_NORMAL );
        } else {
            person.setCategory( Person.CATEGORY_OLD );
        }
    }
}
