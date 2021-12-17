package com.replace.replace.api.poc.annotation;

import com.replace.replace.api.crud.Create;
import com.replace.replace.api.crud.Delete;
import com.replace.replace.api.crud.Update;
import com.replace.replace.configuration.poc.TriggerIdentifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface UnmanagedTrigger {

    TriggerIdentifier id();


    Trigger[] triggers() default {};


    Class< ? extends Create< ? > > createExecutor() default DefaultCreate.class;


    Class< ? extends Update< ? > > updateExecutor() default DefaultUpdate.class;


    Class< ? extends Delete< ? > > deleteExecutor() default DefaultDelete.class;


    Class< ? extends com.replace.replace.api.poc.api.UnmanagedTrigger > unmanagedExecutor() default DefaultUnmanagedExecutor.class;
}
