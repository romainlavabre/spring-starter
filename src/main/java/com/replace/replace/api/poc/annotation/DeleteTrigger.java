package com.replace.replace.api.poc.annotation;

import com.replace.replace.configuration.dynamic.TriggerIdentifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface DeleteTrigger {

    TriggerIdentifier id();


    Trigger[] triggers() default {};


    Class< ? extends com.replace.replace.api.crud.Delete< ? > > executor() default DefaultDelete.class;
}
