package com.replace.replace.api.dynamic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface GetOne {
    String route() default "";


    boolean enabled() default false;


    String[] roles() default {"*"};


    boolean authenticated() default true;
}
