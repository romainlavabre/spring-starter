package com.replace.replace.api.dynamic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface EntryPoint {

    Get get() default @Get;


    Post[] post() default {};


    Patch[] patch() default {};


    Put[] put() default {};


    Delete[] delete() default {};


    Trigger[] triggers() default {};
}
