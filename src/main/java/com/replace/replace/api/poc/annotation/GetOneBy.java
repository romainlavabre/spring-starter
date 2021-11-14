package com.replace.replace.api.poc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface GetOneBy {
    String route() default "";


    Class< ? > entity();


    String method() default "";


    String[] roles() default {"*"};


    boolean authenticated() default true;
}
