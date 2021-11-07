package com.replace.replace.api.dynamic.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface Post {
    String route() default "";


    String[] roles() default {"*"};


    boolean authenticated() default true;


    String[] fields() default {"*"};


    Trigger[] triggers() default {};


    Class< ? extends com.replace.replace.api.crud.Create< ? > > executor() default DefaultCreate.class;
}
