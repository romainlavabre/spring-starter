package com.replace.replace.module.person.trigger;

import com.replace.replace.api.dynamic.api.TriggerResolver;
import com.replace.replace.configuration.dynamic.TriggerIdentifier;
import com.replace.replace.entity.Person;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
public class CategoryTrigger implements TriggerResolver< Person > {

    @Override
    public TriggerIdentifier getId() {
        return TriggerIdentifier.PERSON_CATEGORY;
    }


    @Override
    public Object getResource( Person subject ) {
        return subject;
    }
}
