package com.replace.replace.api.dynamic.annotation;

import com.replace.replace.configuration.dynamic.TriggerIdentifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface CreateTrigger {

    TriggerIdentifier id();


    String[] fields() default {"*"};


    boolean setByArray() default true;


    TriggerIdentifier[] createTriggers() default {};


    TriggerIdentifier[] updateTriggers() default {};


    TriggerIdentifier[] deleteTriggers() default {};


    TriggerIdentifier[] unmanagedTriggers() default {};


    Class< ? extends com.replace.replace.api.crud.Create< ? > > executor() default DefaultCreate.class;
}
