package com.replace.replace.api.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface EntryPoint {

    GetOne getOne() default @GetOne;


    GetAll getAll() default @GetAll;


    GetOneBy[] getOneBy() default {};


    GetAllBy[] getAllBy() default {};


    Post[] post() default {};


    Patch[] patch() default {};


    Put[] put() default {};


    Delete[] delete() default {};


    CreateTrigger[] createTriggers() default {};


    UpdateTrigger[] updateTriggers() default {};


    DeleteTrigger[] deleteTriggers() default {};


    UnmanagedTrigger[] unmanagedTriggers() default {};
}
