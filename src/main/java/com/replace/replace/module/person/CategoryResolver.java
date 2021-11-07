package com.replace.replace.module.person;

import com.replace.replace.api.crud.Update;
import com.replace.replace.api.history.HistoryHandler;
import com.replace.replace.api.request.Request;
import com.replace.replace.entity.Person;
import org.springframework.stereotype.Service;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Service( "PersonCategoryResolver" )
public class CategoryResolver implements Update< Person > {

    protected final HistoryHandler historyHandler;


    public CategoryResolver( HistoryHandler historyHandler ) {
        this.historyHandler = historyHandler;
    }


    @Override
    public void update( Request request, Person person ) {

        if ( person.getAge() <= 20 ) {
            person.setCategory( Person.CATEGORY_YOUNG );
        } else if ( person.getAge() <= 50 ) {
            person.setCategory( Person.CATEGORY_NORMAL );
        } else {
            person.setCategory( Person.CATEGORY_OLD );
        }

        historyHandler.update( person, "category" );
    }
}
