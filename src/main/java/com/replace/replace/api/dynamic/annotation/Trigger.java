package com.replace.replace.api.dynamic.annotation;

import com.replace.replace.api.dynamic.api.TriggerResolver;
import com.replace.replace.configuration.dynamic.TriggerIdentifier;

public @interface Trigger {

    TriggerIdentifier id();


    Class< ? extends TriggerResolver< ? > >[] triggers() default {};


    Class< ? extends com.replace.replace.api.crud.Create< ? > > create() default DefaultCreate.class;


    Class< ? extends com.replace.replace.api.crud.Update< ? > > update() default DefaultUpdate.class;


    Class< ? extends com.replace.replace.api.crud.Delete< ? > > delete() default DefaultDelete.class;
}
